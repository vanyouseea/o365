package hqr.o365.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import hqr.o365.dao.TaMasterCdRepo;
import hqr.o365.domain.TaMasterCd;
import hqr.o365.service.SendLoginMsgToWx;
import hqr.o365.service.TaUserDetailsService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {
    /**
     * 注入我们自己定义的用户信息获取对象
     */
    @Autowired
    private TaUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TaMasterCdRepo tmc;
    
    @Autowired
    private SendLoginMsgToWx send;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // TODO Auto-generated method stub
        String userName = authentication.getName();// 这个获取表单输入中返回的用户名;
        String password = (String) authentication.getCredentials();// 这个是表单中输入的密码；
        // 这里构建来判断用户是否存在和密码是否正确
        //System.out.println("getDetails:"+authentication.getDetails());
        String remoteIP = "UnKnown";
        try {
            WebAuthenticationDetails webDtls = (WebAuthenticationDetails)authentication.getDetails();
            remoteIP = webDtls.getRemoteAddress();
        }
        catch (Exception e) {}

        UserDetails userInfo = userDetailsService.loadUserByUsername(userName); // 这里调用我们的自己写的获取用户的方法；
        if (userInfo == null) {
            throw new BadCredentialsException("用户名不存在");
        }
        boolean flag = passwordEncoder.matches(password,passwordEncoder.encode(userInfo.getPassword()));
        if (!flag) {
            throw new BadCredentialsException("密码不正确");
        }
        else {
        	System.out.println("密码正确");
        	
        	Optional<TaMasterCd> opt1 = tmc.findById("WX_CALLBACK_IND");
        	if(opt1.isPresent()) {
        		String indicator = opt1.get().getCd();
        		if("Y".equals(indicator)) {
        			try {
        				tmc.deleteById("USER_RESPONSE");
        			}
        			catch (Exception e) {}
        			send.sendMsg(remoteIP);
        			
        			boolean isAllow = false;
        			for(int i=0;i<60;i++) {
        				try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {}
        				Optional<TaMasterCd> opt2 = tmc.findById("USER_RESPONSE");
        				if(opt2.isPresent()) {
        					String reply = opt2.get().getCd();
        					if(reply!=null&&!"".equals(reply)) {
        						if("Y".equals(reply)) {
        							isAllow = true;
        						}
        						break;
        					}
        				}
        				else {
        					break;
        				}
        			}
        			//what ever the reply, delete key ty
        			try {
        				tmc.deleteById("USER_RESPONSE");
        			}
        			catch (Exception e) {}
        			
        			if(!isAllow) {
        				throw new BadCredentialsException("登录未获得批准");
        			}
        		}
        	}
        }
        
        Collection<? extends GrantedAuthority> authorities = userInfo.getAuthorities();
        // 构建返回的用户登录成功的token
        return new UsernamePasswordAuthenticationToken(userInfo, password, authorities);
        //return new UsernamePasswordAuthenticationToken(userInfo, null,authorities);
    }
    @Override
    public boolean supports(Class<?> authentication) {
        // 这里直接改成retrun true;表示是支持这个执行
        return true;
    }
}
