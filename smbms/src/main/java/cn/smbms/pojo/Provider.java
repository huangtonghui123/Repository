package cn.smbms.pojo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import java.util.Date;

@Data
public class Provider {
	
	private Integer id;   //id
	@NotEmpty(message = "供应商编码不能为空")
	private String proCode; //供应商编码
	@NotEmpty(message = "供应商名称不能为空")
	private String proName; //供应商名称
	private String proDesc; //供应商描述
	@NotEmpty(message = "供应商联系人不能为空")
	private String proContact; //供应商联系人
	@Length(min = 11,max = 11,message = "请输入正确格式的手机号")
	private String proPhone; //供应商电话
	private String proAddress; //供应商地址
	private String proFax; //供应商传真
	private Integer createdBy; //创建者
	private Date creationDate; //创建时间
	private Integer modifyBy; //更新者
	private Date modifyDate;//更新时间

	private String idPicPath;//组织机构代码证
	private String workPicPath;//企业营业执照

}
