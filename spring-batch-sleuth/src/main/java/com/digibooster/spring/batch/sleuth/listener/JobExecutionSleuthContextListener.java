package com.digibooster.spring.batch.sleuth.listener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import com.digibooster.spring.batch.listener.JobExecutionContextListener;
import com.digibooster.spring.batch.sleuth.SpanInfoHolder;
import com.digibooster.spring.batch.util.CustomJobParameter;

/**
 * This class restores sleuth context inside the Spring batch job
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
public class JobExecutionSleuthContextListener implements JobExecutionContextListener{

	private final Logger log = LoggerFactory.getLogger(JobExecutionSleuthContextListener.class);
	private static final String SLEUTH_PARAM_NAME="sleuth-param";
	
	private final Tracer tracer;
	
	private static final ThreadLocal<Span> ORIGINAL_CONTEXT = new ThreadLocal<>();
	
	public JobExecutionSleuthContextListener(Tracer tracer) {
		this.tracer= tracer;
	}
	

	@Override
	public void insertContextInfo(JobParametersBuilder jobParametersBuilder) {
		log.debug("Save the sleuth context");
		 Span currentSpan= tracer.getCurrentSpan();
		 SpanInfoHolder infoHolder= new SpanInfoHolder();
		 infoHolder.setName(currentSpan.getName());
		 infoHolder.setTraceIdHigh(currentSpan.getTraceIdHigh());
		 infoHolder.setTraceId(currentSpan.getTraceId());
		 infoHolder.setParents(currentSpan.getParents());
		 infoHolder.setSpanId(currentSpan.getSpanId());
		 infoHolder.setRemote(currentSpan.isRemote());
		 infoHolder.setExportable(currentSpan.isExportable());
		 infoHolder.setTags(currentSpan.tags());
		 infoHolder.setProcessId(currentSpan.getProcessId());
		 infoHolder.setBaggage(currentSpan.getBaggage());
		 jobParametersBuilder.addParameter(SLEUTH_PARAM_NAME,  new CustomJobParameter<SpanInfoHolder>(infoHolder));
	}


	@Override
	public void fillJobExecutionContext(JobExecution jobExecution) {
		log.debug("Restore the scurity context");
		CustomJobParameter<SpanInfoHolder> spanParameter= (CustomJobParameter<SpanInfoHolder>) jobExecution.getJobParameters().getParameters().get(SLEUTH_PARAM_NAME);
		if(spanParameter!=null) {
			SpanInfoHolder infoHolder = (SpanInfoHolder)spanParameter.getValue();
			Span newSpan= Span
					.builder()
					.name(infoHolder.getName())
					.traceIdHigh(infoHolder.getTraceIdHigh())
					.traceId(infoHolder.getTraceId())
					.parents(infoHolder.getParents())
					.spanId(infoHolder.getSpanId())
					.remote(infoHolder.isRemote())
					.exportable(infoHolder.isExportable())
					.tags(infoHolder.getTags())
					.processId(infoHolder.getProcessId())
					.baggage(infoHolder.getBaggage())
					.build();
			jobExecution.getExecutionContext().put(SLEUTH_PARAM_NAME, newSpan);
			
		}
		else {
			log.error("Could not find the key {} in job parameters",SLEUTH_PARAM_NAME);
		}
		
	}


	@Override
	public void removeFromJobExecutionContext(JobExecution jobExecution) {
		jobExecution.getExecutionContext().remove(SLEUTH_PARAM_NAME);
	}


	@Override
	public void restoreContext(StepExecution stepExecution) {
		if(stepExecution.getJobExecution().getExecutionContext().containsKey(SLEUTH_PARAM_NAME)) {
			Span newSpan=(Span) stepExecution.getJobExecution().getExecutionContext().get(SLEUTH_PARAM_NAME);
			Span originalSpan= tracer.getCurrentSpan();
			ORIGINAL_CONTEXT.set(originalSpan);
			tracer.continueSpan(newSpan);
		}
		else {
			log.error("Could not find key {} in the job execution context",SLEUTH_PARAM_NAME);
		}
		
	}


	@Override
	public void clearContext(StepExecution stepExecution) {
		Span originalSpan = ORIGINAL_CONTEXT.get();
		if(originalSpan!=null) {
			tracer.continueSpan(originalSpan);
			ORIGINAL_CONTEXT.remove();
		}
		
	}
	
	
}
