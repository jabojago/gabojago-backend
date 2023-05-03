package com.example.gabojago_server.web.controller.restDocs;

import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class RestDocsUtils {


    public static List<FieldDescriptor> pageableDocsWithDefaults() {
        ArrayList<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                List.of(
                        fieldWithPath("last").description("마지막 페이지 여부"),
                        fieldWithPath("first").description("첫번째 페이지 여부"),
                        fieldWithPath("totalPages").description("전체 페이지 수"),
                        fieldWithPath("totalElements").description("전체 Content 수"),
                        fieldWithPath("size").description("페이지 당 Content 수"),
                        fieldWithPath("number").description("현재 페이지"),
                        fieldWithPath("numberOfElements").description("현재 페이지의 Content 수"),
                        fieldWithPath("empty").description("비어 있는지"),
                        fieldWithPath("sort.empty").description("비어 있는지"),
                        fieldWithPath("sort.unsorted").description("비정렬 여부"),
                        fieldWithPath("sort.sorted").description("정렬 여부")
                ));
        fieldDescriptors.addAll(pageableDocs());
        return fieldDescriptors;
    }

    public static List<FieldDescriptor> pageableDocs() {
        return new ArrayList<>(
                List.of(
                        fieldWithPath("pageable.sort.empty").description("비어 있는지"),
                        fieldWithPath("pageable.sort.sorted").description("정렬 여부"),
                        fieldWithPath("pageable.sort.unsorted").description("비정렬 여부"),
                        fieldWithPath("pageable.offset").description("offset"),
                        fieldWithPath("pageable.pageNumber").description("현재 페이지 번호"),
                        fieldWithPath("pageable.pageSize").description("현재 페이지 크기"),
                        fieldWithPath("pageable.paged").description("페이지 여부"),
                        fieldWithPath("pageable.unpaged").description("페이지 여부")
                ));
    }

    public static List<FieldDescriptor> pageableDocsWithContent(Map<String, String> attributes) {
        List<FieldDescriptor> fieldDescriptors = pageableDocsWithDefaults();
        fieldDescriptors.addAll(onlyContent(attributes));
        return fieldDescriptors;
    }

    public static List<FieldDescriptor> onlyContent(Map<String, String> attributes) {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
        for (var path : attributes.keySet()) {
            String description = attributes.get(path);
            fieldDescriptors.add(fieldWithPath(path).description(description));
        }
        return fieldDescriptors;
    }

}
