package hqr.o365.service;

import java.io.StringReader;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

import hqr.o365.dao.TaMasterCdRepo;
import hqr.o365.domain.TaMasterCd;

@Service
public class CallBackService {

	@Autowired
	private TaMasterCdRepo tmc;
	
	public String handleCallBack(HttpServletRequest request, String body) {
		//GET -> bind , POST -> get msg
		String method = request.getMethod();
		String result = "";
		
		Optional<TaMasterCd> opt0 = tmc.findById("WX_CALLBACK_IND");
		if(opt0.isPresent()) {
			String indicator = opt0.get().getCd();
			if("Y".equals(indicator)) {
				Optional<TaMasterCd> opt1 = tmc.findById("WX_CALLBACK_TOKEN");
				Optional<TaMasterCd> opt2 = tmc.findById("WX_CORPID");
				Optional<TaMasterCd> opt3 =tmc.findById("WX_CALLBACK_AESKEY");
				
				if(opt1.isPresent()&&opt2.isPresent()&&opt3.isPresent()) {
					
					String sToken = opt1.get().getCd();
					String sCorpID = opt2.get().getCd();
					String sEncodingAESKey = opt3.get().getCd();
					
					if("GET".equals(method)) {
						String sVerifyMsgSig = request.getParameter("msg_signature");
						String sVerifyTimeStamp = request.getParameter("timestamp");
						String sVerifyNonce = request.getParameter("nonce");
						String sVerifyEchoStr = request.getParameter("echostr");

						try {
							WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
							result = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sVerifyEchoStr);
						} catch (AesException e) {
							e.printStackTrace();
						}
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
							
							//check if have key_ty USER_RESPONSE in DB, if no, there is no login request, if yes, update the user response in db
							Optional<TaMasterCd> opt4 = tmc.findById("USER_RESPONSE");
							if(opt4.isPresent()) {
								TaMasterCd enti = opt4.get();
								content = content.trim().toUpperCase();
								enti.setCd(content);
								enti.setDecode(sender);
								tmc.saveAndFlush(enti);
							}
							else {
								System.out.println("ignore the msg");
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						result = "ok";
					}
					else {
						System.out.println("Not support method:"+ method);
						result = "Not support method:"+ method;
					}
				}
				else {
					System.out.println("No callback config, ignore it");
				}
			}
			else {
				System.out.println("wx login verify indicator is not Y");
			}
		}
		else {
			System.out.println("No wx login verify");
		}
		
		return result;
	}
}
