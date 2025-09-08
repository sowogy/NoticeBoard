// 간단한 클라이언트 검증 & 미리보기
(function () {
    const input = document.getElementById("fileInput");
    const list = document.getElementById("fileList");
    const err  = document.getElementById("fileError");

    if (!input) return;

    const MAX_FILES = 10;
    const MAX_SIZE = 20 * 1024 * 1024; // 20MB
    const ALLOWED = [".ppt",".pptx",".pdf",".hwp",".hwpx",".doc",".docx"];

    input.addEventListener("change", () => {
        err.style.display = "none";
        err.textContent = "";
        list.innerHTML = "";

        const files = Array.from(input.files || []);
        const problems = [];

        if (files.length > MAX_FILES) {
            problems.push(`파일은 최대 ${MAX_FILES}개까지 업로드할 수 있습니다.`);
        }

        files.forEach((f) => {
            const name = f.name.toLowerCase();
            const ext  = name.slice(name.lastIndexOf("."));
            if (!ALLOWED.includes(ext)) {
                problems.push(`허용되지 않는 형식: ${f.name}`);
            }
            if (f.size > MAX_SIZE) {
                problems.push(`용량 초과(20MB): ${f.name}`);
            }
        });

        if (problems.length) {
            err.textContent = problems.join(" / ");
            err.style.display = "block";
            input.value = ""; // 리셋
            return;
        }

        // 목록 렌더링
        files.forEach((f) => {
            const row = document.createElement("div");
            row.className = "upload-row";
            row.innerHTML = `
        <span class="upload-name" title="${f.name}">${f.name}</span>
        <span class="upload-size">${(f.size/1024/1024).toFixed(2)} MB</span>
      `;
            list.appendChild(row);
        });
    });
})();
