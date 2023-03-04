package by.novik.orderthymeleaf.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Min(0)
    private int clientId;
    private String goods;
    @Length(min = 4, message = "name is too short")
    private String clientName;
    @NotEmpty(message = "address can't be empty")
    private String address;


}
