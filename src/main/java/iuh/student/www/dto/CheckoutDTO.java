package iuh.student.www.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutDTO {

    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String shippingAddress;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Size(max = 15, message = "Số điện thoại không được vượt quá 15 ký tự")
    private String phone;

    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private String notes;

    @NotBlank(message = "Vui lòng chọn phương thức thanh toán")
    @Size(max = 50, message = "Phương thức thanh toán không hợp lệ")
    private String paymentMethod = "COD"; // Mặc định thanh toán khi nhận hàng
}
