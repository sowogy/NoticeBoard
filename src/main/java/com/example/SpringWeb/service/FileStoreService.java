package com.example.SpringWeb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class FileStoreService {

    // application.properties에서 주입
    @Value("${app.upload.dir:uploads}")
    private String rootDir;

    // 허용 확장자
    private static final Set<String> ALLOWED =
            Set.of("ppt","pptx","pdf","hwp","hwpx","doc","docx");

    // 파일당 최대 20MB
    private static final long MAX_SIZE = 20L * 1024 * 1024;

    public List<StoredFile> storeAll(List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) return List.of();

        // 날짜별 디렉터리(ex: uploads/2025-09-01)
        Path base = Paths.get(rootDir, LocalDate.now().toString()).toAbsolutePath().normalize();
        Files.createDirectories(base);

        List<StoredFile> result = new ArrayList<>();
        for (MultipartFile mf : files) {
            if (mf.isEmpty()) continue;

            String original = StringUtils.cleanPath(Objects.requireNonNull(mf.getOriginalFilename()));
            String ext = getExtension(original).toLowerCase(Locale.ROOT);

            if (!ALLOWED.contains(ext)) {
                throw new IllegalArgumentException("허용되지 않는 확장자: " + original);
            }
            if (mf.getSize() > MAX_SIZE) {
                throw new IllegalArgumentException("용량 초과(20MB): " + original);
            }

            String stored = UUID.randomUUID().toString() + "." + ext;
            Path target = base.resolve(stored);

            // 저장
            Files.copy(mf.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            StoredFile meta = new StoredFile(
                    original,
                    stored,
                    "/uploads/" + LocalDate.now() + "/" + stored, // 웹 접근 경로
                    mf.getContentType(),
                    mf.getSize()
            );
            result.add(meta);
        }
        return result;
    }

    private static String getExtension(String filename) {
        int i = filename.lastIndexOf('.');
        return (i == -1) ? "" : filename.substring(i + 1);
    }

    public void deleteFile(String fileUrl, String storedName){
        try {
            Path p = resolveStoredPath(fileUrl, storedName);
            if (p != null) {
                Files.deleteIfExists(p);
            }
        } catch (Exception ignore) {
            // 여기서는 조용히 무시 (로그 시스템이 있으면 warn 로그로 남기기 권장)
            // log.warn("이전 첨부파일 삭제 실패: {}", storedName, ex);
        }
    }

    private Path resolveStoredPath(String fileUrl, String storedName){
        if (fileUrl == null || storedName == null) return null;

        final String prefix = "/uploads/";
        int idx = fileUrl.indexOf(prefix);
        if (idx < 0) return null;

        String tail = fileUrl.substring(idx + prefix.length()); // "2025-09-02/uuid.pdf"
        int slash = tail.indexOf('/');
        if (slash < 0) return null;

        String dateFolder = tail.substring(0, slash); // "2025-09-02"
        // rootDir 기준 절대경로 정규화
        return Paths.get(rootDir).toAbsolutePath().normalize()
                .resolve(dateFolder)
                .resolve(storedName);
    }

    /** 응답/DB에 사용 가능한 간단 메타 모델 */
    public record StoredFile(
            String originalName,
            String storedName,
            String publicUrl,     // 예: /uploads/2025-09-01/uuid.pdf
            String contentType,
            long size
    ) {}
}
