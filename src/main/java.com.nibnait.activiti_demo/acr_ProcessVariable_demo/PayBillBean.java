package acr_ProcessVariable_demo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PayBillBean implements Serializable {
    private Integer id;
    private Integer cost;//金额
    private String payPerson;//申请人
    private Date date;//申请日期
}
