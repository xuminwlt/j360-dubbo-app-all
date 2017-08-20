package me.j360.dubbo.batch.task;

import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.modules.util.time.DateFormatUtil;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;


@Slf4j
@Component
public class JobTaskScheduled {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private Job batchJob;

    private AtomicLong count = new AtomicLong(0);

    public void run() {
        long timeCount = count.incrementAndGet();
        log.info("任务执行开始 [count={}, date={}]",timeCount, DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, new Date()));
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        launcher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        try {
            launcher.run(batchJob, new JobParameters());
        } catch (Exception e) {
            log.error("任务执行失败 [count={}, date={}]", timeCount, DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, new Date()),e);
        } finally {
            log.info("任务执行结束 [count={}, date={}]",timeCount, DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, new Date()));
        }

    }
}
