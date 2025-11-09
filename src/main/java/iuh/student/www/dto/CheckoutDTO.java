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

    @NotBlank(message = "Shipping address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String shippingAddress;

    @NotBlank(message = "Phone number is required")
    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    private String phone;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}
