package com.dist.bdf.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.facade.service.security.SecurityParaService;
import com.dist.bdf.facade.service.security.SecurityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = { "API-安全服务模块" }, description = "SecurityController")
@RestController
@RequestMapping(value = "/rest/sysservice/security")
//@CrossOrigin(origins = "*")
public class SecurityController extends BaseController {

	@Autowired
	private SecurityParaService secureParaService;
	@Autowired
	private SecurityService secureService;

	@ApiOperation(value = "获取rsa的模参数", notes = "getDefaultPublicModulusHex")
	@RequestMapping(value = "/rsa/modulus", method = { RequestMethod.GET })
	public Result getDefaultPublicModulusHex() {

		return super.successResult(this.secureParaService.getDefaultPublicModulusHex());
	}

	@ApiOperation(value = "获取rsa的指数参数", notes = "getDefaultPublicExponentHex")
	@RequestMapping(value = "/rsa/exponent", method = { RequestMethod.GET })
	public Result getDefaultPublicExponentHex() {

		return super.successResult(this.secureParaService.getDefaultPublicExponentHex());
	}

	@ApiOperation(value = "获取rsa的模和指数参数", notes = "getDefaultPublicModulusAndExponentHex")
	@RequestMapping(value = "/rsa/modulus.exponent", method = { RequestMethod.GET })
	public Result getDefaultPublicModulusAndExponentHex() {

		return super.successResult(this.secureParaService.getDefaultPublicModulusAndExponentHex());
	}

	@ApiOperation(value = "rsa加密", notes = "encryptRSA")
	@RequestMapping(value = "/rsa/encrypt/{data}", method = { RequestMethod.GET })
	public Result encryptRSA(@ApiParam(value = "待加密的数据") @PathVariable String data) {

		if (StringUtils.isEmpty(data))
			throw new BusinessException("加密数据不能为空");

		return super.successResult(this.secureService.encryptRSA(data));
	}

/*	@ApiOperation(value = "检测来自微信服务器的信息", notes = "checkWechat，用于接收 get 参数，返回验证参数。" + "开发者通过检验signature对请求进行校验（下面有校验方式）。"
			+ "若确认此次GET请求来自微信服务器，请原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败。")
	@RequestMapping(value = "/wechat", method = RequestMethod.GET)
	public void checkWechat(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(value = "微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。") @RequestParam(value = "signature", required = true) String signature,
			@ApiParam(value = "时间戳") @RequestParam(value = "timestamp", required = true) String timestamp,
			@ApiParam(value = "随机数") @RequestParam(value = "nonce", required = true) String nonce,
			@ApiParam(value = "随机字符串") @RequestParam(value = "echostr", required = true) String echostr)
			throws Exception {

		if (WechatSignUtil.checkSignature(signature, timestamp, nonce)) {
			PrintWriter out = response.getWriter();
			out.print(echostr);
			out.close();
		} else {
			logger.error("认证失败");
			throw new BusinessException("认证失败");
		}
	}*/

	@ApiOperation(value = "接收微信服务端消息", notes = "getMgsFromWechat")
	@RequestMapping(value = "/wechat/mgs", method = RequestMethod.POST)
	public void getMgsFromWechat() {
		System.out.println("微信服务端消息");
	}
}
