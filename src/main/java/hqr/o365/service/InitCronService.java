package hqr.o365.service;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import cn.hutool.cron.CronUtil;

@Service
public class InitCronService {

	@PostConstruct
	public void initTimer() {
		CronUtil.setMatchSecond(true);
		CronUtil.start();
	}

}
