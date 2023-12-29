package com.university.post.controller;

import com.university.post.dataset.DeliveryPointDS;
import com.university.post.dto.*;
import com.university.post.enums.MessageResponse;
import com.university.post.enums.Type;
import com.university.post.exception.entity.EntityException;
import com.university.post.exception.entity.EntityNotFound;
import com.university.post.model.DeliveryPoint;
import com.university.post.model.User;
import com.university.post.payload.request.point.DeliveryPointRequest;
import com.university.post.payload.request.point.GatheringPointRequest;
import com.university.post.payload.request.point.TransactionPointRequest;
import com.university.post.payload.response.data.DataRepsonse;
import com.university.post.payload.response.data.ErrorResponse;
import com.university.post.payload.response.data.PageResponse;
import com.university.post.payload.response.statistic.OrderStatisticPointResponse;
import com.university.post.service.DeliveryPointService;
import com.university.post.service.OrderStatsticsService;
import com.university.post.service.UserService;
import com.university.post.utils.Constant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@SecurityRequirement(name = "Xác thực Bearer")
public class AdminController {

        @Autowired
        private DeliveryPointService deliveryPointService;

        @Autowired
        private OrderStatsticsService orderStatsticsService;

        @GetMapping("/delivery-points")
        @ResponseStatus(HttpStatus.OK)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lấy thông tin các điểm chuyển phát thành công", content = {
                                        @Content(schema = @Schema(implementation = PageResponse.class), mediaType = "application/json") }),
                        @ApiResponse(responseCode = "403", description = "Không có quyền truy cập", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }),
                        @ApiResponse(responseCode = "401", description = "Xác thực không thành công", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        })
        })
        public DataRepsonse<PageResponse<DeliveryPointDTO>> getDeliveryPoints(DeliveryPointDS deliveryPointDS) {
                Page<DeliveryPoint> deliveryPoints = deliveryPointService.getAllDeliveryPointByPage(deliveryPointDS);
                Page<Object> deliveryPointDTO = deliveryPoints.map(new Function<DeliveryPoint, DeliveryPointDTO>() {
                        @Override
                        public DeliveryPointDTO apply(DeliveryPoint deliveryPoint) {
                                if (deliveryPoint.getTypeResource().equals(Type.TRANSACTION_POINT_LOCATION)) {
                                        return new TransactionPointDTO(deliveryPoint);
                                } else {
                                        return new GatheringPointDTO(deliveryPoint);
                                }
                        }
                });
                return new DataRepsonse<>(HttpStatus.OK, new PageResponse(deliveryPointDTO),
                                "Lấy thông tin các điểm chuyển phát thành công");
        }

        @GetMapping("/delivery-points/{id}")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lấy thông tin điểm chuyển phát thành công", content = {
                                        @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }),
                        @ApiResponse(responseCode = "401", description = "Xác thực không thành công", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }),
                        @ApiResponse(responseCode = "403", description = "Không có quyền truy cập", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }),
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy điểm chuyển phát", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        })
        })
        public DataRepsonse<DeliveryPointDTO> getDeliveryPointById(@PathVariable String id) {
                DeliveryPoint deliveryPoint = deliveryPointService.getDeliveryPointById(id);
                String type = deliveryPoint.getTypeResource();

                DeliveryPointDTO deliveryPointDTO;
                if (type.equals(Type.TRANSACTION_POINT_LOCATION)) {
                        deliveryPointDTO = new TransactionPointDTO(deliveryPoint);
                } else {
                        deliveryPointDTO = new GatheringPointDTO(deliveryPoint);
                }
                return new DataRepsonse<>(HttpStatus.OK, deliveryPointDTO, "Lấy thông tin điểm chuyển phát thành công");
        }

        @PostMapping("/delivery-points")
        @ResponseStatus(HttpStatus.CREATED)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Tạo điểm chuyển phát thành công", content = {
                                        @Content(schema = @Schema(implementation = DeliveryPointDTO.class), mediaType = "application/json") }),
                        @ApiResponse(responseCode = "401", description = "Xác thực không thành công", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }),
                        @ApiResponse(responseCode = "400", description = "Tạo điểm chuyển phát không thành công", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }),
                        @ApiResponse(responseCode = "403", description = "Không có quyền truy cập", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }),
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy user hoặc điểm chuyển phát", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        })
        })
        public DataRepsonse<DeliveryPointDTO> addDeliveryPoint(@RequestBody DeliveryPointRequest deliveryPointRequest) {
                if (!deliveryPointService.isValidationAdminInRegister(deliveryPointRequest)) {
                        throw new EntityException(MessageResponse.SUCCESS_REGISTER.getMessage());
                }
                DeliveryPoint deliveryPoint = deliveryPointService.handleRegister(deliveryPointRequest);
                deliveryPointService.addDeliveryPoint(deliveryPoint);

                DeliveryPointDTO deliveryPointDTO;
                String type = deliveryPointRequest.getType();
                if (type.equals(Type.TRANSACTION_POINT_LOCATION)) {
                        deliveryPointDTO = new TransactionPointDTO(deliveryPoint);
                } else {
                        deliveryPointDTO = new GatheringPointDTO(deliveryPoint);
                }
                return new DataRepsonse<>(HttpStatus.CREATED, deliveryPointDTO, "Tạo điểm chuyển phát thành công");
        }

        @PutMapping("/delivery-points/{id}")
        @ResponseStatus(HttpStatus.OK)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cập nhật điểm chuyển phát thành công", content = {
                                        @Content(schema = @Schema(implementation = DeliveryPointDTO.class), mediaType = "application/json") }),
                        @ApiResponse(responseCode = "401", description = "Xác thực không thành công", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }),
                        @ApiResponse(responseCode = "403", description = "Không có quyền truy cập", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }),
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy user hoặc điểm chuyển phát", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        })
        })
        public DataRepsonse<DeliveryPointDTO> updateDeliveryPoint(@PathVariable String id,
                        @RequestBody DeliveryPointRequest deliveryPointRequest) {
                DeliveryPoint deliveryPoint = deliveryPointService.handleUpdate(id, deliveryPointRequest);
                deliveryPointService.updateDeliveryPoint(deliveryPoint);

                DeliveryPointDTO deliveryPointDTO;
                String type = deliveryPointRequest.getType();
                if (type.equals(Type.TRANSACTION_POINT_LOCATION)) {
                        deliveryPointDTO = new TransactionPointDTO(deliveryPoint);
                } else {
                        deliveryPointDTO = new GatheringPointDTO(deliveryPoint);
                }
                return new DataRepsonse<>(HttpStatus.CREATED, deliveryPointDTO, "Cập nhật điểm chuyển phát thành công");
        }

        @DeleteMapping("/delivery-points/{id}")
        @ResponseStatus(HttpStatus.OK)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Xóa điểm chuyển phát thành công"),
                        @ApiResponse(responseCode = "401", description = "Xác thực không thành công", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }),
                        @ApiResponse(responseCode = "403", description = "Không có quyền truy cập", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }),
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy điểm chuyển phát", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        })
        })
        public DataRepsonse<String> deleteDelieryPoint(@PathVariable String id) {
                DeliveryPoint deliveryPoint = deliveryPointService.getDeliveryPointById(id);
                deliveryPointService.deleteDeliveryPoint(deliveryPoint);
                return new DataRepsonse<>(HttpStatus.OK, "", "Xóa điểm chuyển phát thành công");
        }

        @GetMapping("/order-statistics")
    public DataRepsonse<List> getOrderStatistics(String typePoint, @RequestParam(required = false) String pointId) {
        List<OrderStatisticPointResponse> orderStastics = orderStatsticsService.getOrderStastic(typePoint, pointId);

        return new DataRepsonse<>(HttpStatus.OK,
                orderStastics, "Thống kê đơn hàng thành công");
    }
}
