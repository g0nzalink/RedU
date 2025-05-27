package com.example.backendredu.publicacion.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class TagListConverter implements AttributeConverter<List<Tag>, String> {
	
	@Override
	public String convertToDatabaseColumn(List<Tag> tag_list) {
		if (tag_list.isEmpty()) { return ""; }
		return tag_list.stream().map(Enum::name).collect(Collectors.joining(","));
	}
	
	@Override
	public List<Tag> convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.isEmpty()) return new ArrayList<>();
		return Arrays.stream(dbData.split(","))
				.map(Tag::valueOf)
				.collect(Collectors.toList());
	}
}
