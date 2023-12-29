package com.university.post.controller;

import com.university.post.dataset.UserDS;
import com.university.post.dto.UserDTO;
import com.university.post.enums.MessageResponse;
import com.university.post.exception.user.UserException;
import com.university.post.model.*;
import com.university.post.payload.request.user.UserRequest;
import com.university.post.payload.response.data.DataRepsonse;
import com.university.post.payload.response.data.ErrorResponse;
import com.university.post.payload.response.data.PageResponse;
import com.university.post.service.*;
import com.university.post.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Converter;
import javax.validation.Valid;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/management/staffs")
@SecurityRequirement(name = "Xác thực Bearer")
public class ManagementController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "Lấy thông tin các nhân viên thành công",
                    content = { @Content(schema = @Schema(implementation = UserDTO.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode  = "401", description = "Xác thực không thành công", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode  = "403", description = "Không có quyền truy cập", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public DataRepsonse<PageResponse> getStaffs(UserDS userDS) {
        Page<User> users = userService.getAllUser(userDS);
        Page<UserDTO> userDTOs = users.map(new Function<User, UserDTO>() {
            @Override
            public UserDTO apply(User user) {
                return new UserDTO(user);
            }
        });
        return new DataRepsonse<>(HttpStatus.OK,new PageResponse<>(userDTOs) , "Lấy thông tin các nhân viên thành công");
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "Lấy thông tin nhân viên thành công",
                    content = { @Content(schema = @Schema(implementation = UserDTO.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode  = "401", description = "Xác thực không thành công", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode  = "403", description = "Không có quyền truy cập", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode  = "404", description = "Không tìm thấy user", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public DataRepsonse<UserDTO> getStaff(@PathVariable String ìd) {
        return new DataRepsonse<>(HttpStatus.OK, new UserDTO(userService.getUserById(ìd)), "Lấy thông tin nhân viên thành công");
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "Cập nhật nhân viên thành công",
                    content = { @Content(schema = @Schema(implementation = UserDTO.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode  = "401", description = "Xác thực không thành công", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode  = "403", description = "Không có quyền truy cập", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode  = "404", description = "Không tìm thấy user", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public DataRepsonse<UserDTO> updateStaff(@PathVariable String id, @RequestBody UserRequest userRequest) {
        User user = userService.updateUser(id, userRequest);
        return new DataRepsonse<>(HttpStatus.OK, new UserDTO(user), "Cập nhật nhân viên thành công");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "Xóa nhân viên thành công"),
            @ApiResponse(responseCode  = "401", description = "Xác thực không thành công", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode  = "403", description = "Không có quyền truy cập", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode  = "404", description = "Không tìm thấy user", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public DataRepsonse<String> deleteStaff(@PathVariable String id) {
        userService.deleteUser(userService.getUserById(id));
        return new DataRepsonse<>(HttpStatus.OK, "", "Xóa nhân viên thành công");
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Tạo nhân viên")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "201", description = "Đăng ký thành công",
                    content = { @Content(schema = @Schema(implementation = UserDTO.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode  = "401", description = "Xác thực không thành công", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode  = "400", description = "Đăng ký không thành công", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode  = "403", description = "Không có quyền truy cập", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode  = "404", description = "Không tìm thấy role", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public DataRepsonse<UserDTO> addStaff(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
        if (!userService.checkValidUser(userRequest) || bindingResult.hasErrors()) {
            throw new UserException(MessageResponse.ERROR_REGISTER.getMessage());
        }
        User user = userService.addUser(userRequest);
        return new DataRepsonse<>(HttpStatus.CREATED, new UserDTO(user), MessageResponse.SUCCESS_REGISTER.getMessage());
    }

}
