package com.fatiny.combine.core.param;

import java.util.HashMap;
import java.util.Map;

public class ParamField {

	private String prefix;
	private String mainWord;
	private String keyIndex;
	private String field;
	private String value;

	// Key:filed, value:propertiy.value
	private Map<String, String> field2value;

	public ParamField(String prefix, String mainWord, String keyIndex, String field, String value) {
		super();
		this.prefix = prefix;
		this.mainWord = mainWord;
		this.keyIndex = keyIndex;
		this.field = field;
		this.value = value;
		this.field2value = new HashMap<String, String>();
	}

	// public ParamField(String mainWord, String keyIndex, String field, String
	// value) {
	// super();
	// this.mainWord = mainWord;
	// this.keyIndex = keyIndex;
	// this.field = field;
	// this.value = value;
	// }

	public ParamField(String mainWord, String keyIndex, Map<String, String> field2value) {
		super();
		this.mainWord = mainWord;
		this.keyIndex = keyIndex;
		this.field2value = field2value;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public ParamField() {
		super();
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMainWord() {
		return mainWord;
	}

	public void setMainWord(String mainWord) {
		this.mainWord = mainWord;
	}

	public String getKeyIndex() {
		return keyIndex;
	}

	public void setKeyIndex(String keyIndex) {
		this.keyIndex = keyIndex;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ParamField [prefix=" + prefix + ", mainWord=" + mainWord + ", keyIndex=" + keyIndex + ", field=" + field + ", value=" + value + "]";
	}

}
