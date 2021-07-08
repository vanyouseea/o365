package hqr.o365.ctrl;

import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

import hqr.o365.service.GetGlobalInd;

@Controller
public class IndexCtrl {
	
	@Autowired
	private GetGlobalInd ggl;
	
	@RequestMapping(value = {"/","/index.html"})
	public String index(HttpServletRequest request) {
		String ind = ggl.getIndicator();
		//request.getSession().setAttribute("tmc", tmc);
		if("Y".equals(ind)){
			System.out.println("Allow to reg admin");
			return "reg";
		}
		else {
			System.out.println("Not allow to reg admin");
			return "login";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = {"/callback"})
	public String callback(HttpServletRequest request, @RequestBody String body ){
		//GET -> bind , POST -> get msg
		String method = request.getMethod();
		
		String sToken = "A0cQOwx1vcTHPWJOiyUipjvk2JDw3";
		String sCorpID = "ww13a3890a2c0815e3";
		String sEncodingAESKey = "rrtr5fIcKnyXwm3OOCLoarvrxT1EJ6PTFU3cBnaOknN";
		
		if("GET".equals(method)) {
			String sVerifyMsgSig = request.getParameter("msg_signature");
			String sVerifyTimeStamp = request.getParameter("timestamp");
			String sVerifyNonce = request.getParameter("nonce");
			String sVerifyEchoStr = request.getParameter("echostr");

			String sEchoStr = "";
			try {
				WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
				sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sVerifyEchoStr);
			} catch (AesException e) {
				e.printStackTrace();
			}
			return sEchoStr;
		}
		else if("POST".equals(method)) {
			String sReqMsgSig = request.getParameter("msg_signature");
			String sReqTimeStamp = request.getParameter("timestamp");
			String sReqNonce = request.getParameter("nonce");
			String sReqData = body;
			try {
				WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
				String sMsg = wxcpt.DecryptMsg(sReqMsgSig, sReqTimeStamp, sReqNonce, sReqData);
				
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				StringReader sr = new StringReader(sMsg);
				Document document = db.parse(new InputSource(sr));

				Element root = document.getDocumentElement();
				NodeList nodelist1 = root.getElementsByTagName("FromUserName");
				String sender = nodelist1.item(0).getTextContent();
				NodeList nodelist2 = root.getElementsByTagName("Content");
				String content = nodelist2.item(0).getTextContent();
				System.out.println("Sender is " + sender);
				System.out.println("Content is " + content);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "ok";
		}
		else {
			System.out.println("Not support method:"+ method);
			return "Not support method:"+ method;
		}
	}
	
}
