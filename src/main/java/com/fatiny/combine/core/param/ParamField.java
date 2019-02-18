package com.fatiny.combine.core.param;

public class ParamField {

	private String mainWord;
	private String keyIndex;
	private String field;
	private String value;

	public ParamField(String mainWord, String keyIndex, String field, String value) {
		super();
		this.mainWord = mainWord;
		this.keyIndex = keyIndex;
		this.field = field;
		this.value = value;
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
		return "ParamField [mainWord=" + mainWord + ", keyIndex=" + keyIndex + ", field=" + field + ", value=" + value + "]";
	}

}
