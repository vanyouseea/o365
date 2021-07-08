package hqr.o365.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import hqr.o365.service.CallBackService;
import hqr.o365.service.GetGlobalInd;

@Controller
public class IndexCtrl {
	
	@Autowired
	private GetGlobalInd ggl;
	
	@Autowired
	private CallBackService cbs;
	
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
	public String callback(HttpServletRequest request, @RequestBody(required=false) String body ){
		return cbs.handleCallBack(request, body);
	}
	
}
