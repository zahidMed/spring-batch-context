package com.digibooster.spring.batch.sleuth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.sleuth.Span;

/**
 * Sleuth information holder that is used to store values from the current Span
 * 
 * @see Span
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 */
public class SpanInfoHolder implements Serializable{
	
	private static final long serialVersionUID = -8532072257517004671L;
	
	private String name;
	private long traceIdHigh;
	private long traceId;
	private List<Long> parents = new ArrayList<>();
	private long spanId;
	private boolean remote = false;
	private boolean exportable = true;
	private Map<String, String> tags;
	private String processId;
	private Map<String,String> baggage;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getTraceIdHigh() {
		return traceIdHigh;
	}
	public void setTraceIdHigh(long traceIdHigh) {
		this.traceIdHigh = traceIdHigh;
	}
	public long getTraceId() {
		return traceId;
	}
	public void setTraceId(long traceId) {
		this.traceId = traceId;
	}
	public List<Long> getParents() {
		return parents;
	}
	public void setParents(List<Long> parents) {
		this.parents = parents;
	}
	public long getSpanId() {
		return spanId;
	}
	public void setSpanId(long spanId) {
		this.spanId = spanId;
	}
	public boolean isRemote() {
		return remote;
	}
	public void setRemote(boolean remote) {
		this.remote = remote;
	}
	public boolean isExportable() {
		return exportable;
	}
	public void setExportable(boolean exportable) {
		this.exportable = exportable;
	}
	public Map<String, String> getTags() {
		return tags;
	}
	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public Map<String, String> getBaggage() {
		return baggage;
	}
	public void setBaggage(Map<String, String> baggage) {
		this.baggage = baggage;
	}
}
