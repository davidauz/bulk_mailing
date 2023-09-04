//package com.davidauz.blkm_read_d.controller;
//
//
//import com.davidauz.blkm_read_d.blkm_read_task;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController // Controller + @ResponseBody
//public class REST_api {
//
//	private final blkm_read_task myScheduledTask;
//
//	public REST_api(blkm_read_task myScheduledTask) {
//		this.myScheduledTask = myScheduledTask;
//	}
//
//	@GetMapping("brd_stop")
//	public String stopTask() {
//		// Stop the scheduled task
//		myScheduledTask.cancel();
//		return "Task stopped";
//	}
//
//	@PostMapping("brd_restart")
//	public String restartTask(@RequestParam long interval) {
//		// Restart the scheduled task with a new interval
//		myScheduledTask.setInterval(interval);
//		myScheduledTask.getScheduler().scheduleAtFixedRate(myScheduledTask::doTask, interval);
//		return "Task restarted with interval " + interval;
//	}
//}
