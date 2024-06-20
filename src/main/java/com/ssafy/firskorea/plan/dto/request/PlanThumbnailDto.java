package com.ssafy.firskorea.plan.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(title = "PlanThumbnailDto", description = "여행 계획 썸네일 이미지 정보")
public class PlanThumbnailDto {
    @Schema(description = "여행 계획 Id")
    @Pattern(regexp = "^\\d+$")
    private String planId;
    @Schema(description = "저장된 폴더명")
    private String saveFolder;
    @Schema(description = "원본 파일명")
    private String originFile;
    @Schema(description = "저장된 파일명")
    private String saveFile;

    public PlanThumbnailDto(String saveFolder, String originFile, String saveFile) {
        this.saveFile = saveFile;
        this.saveFolder = saveFolder;
        this.originFile = originFile;
    }
}
