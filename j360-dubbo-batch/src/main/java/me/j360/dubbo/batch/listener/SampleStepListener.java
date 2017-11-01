package me.j360.dubbo.batch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * Package: me.j360.dubbo.batch.listener
 * User: min_xu
 * Date: 2017/11/1 下午7:56
 * 说明：
 */
public class SampleStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        //使用环境容器自定义变量
        stepExecution.getExecutionContext().putLong("id", 100L);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {


        return null;
    }
}
