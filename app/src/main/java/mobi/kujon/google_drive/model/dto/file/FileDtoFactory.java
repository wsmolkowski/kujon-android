package mobi.kujon.google_drive.model.dto.file;

import java.util.ArrayList;
import java.util.List;

import mobi.kujon.google_drive.model.json.KujonFile;

/**
 *
 */

public class FileDtoFactory {

    private static final String IMAGE_CONTENT_FILE = "image";
    private static final String VIDEO_CONTENT_FILE = "video";
    private static final String AUDIO_CONTENT_FILE = "audio";
    private static final String PDF_FILE_CONENT = "pdf";
    private static final String DOC_FILE_CONTENT = "document";
    private static final String PPT_FILE_CONTENT = "presentation";
    private static final String XLS_FILE_CONTENT = "sheet";

    public static FileDTO createFileDto(KujonFile kujonFile){
        String contentType = kujonFile.contentType;

        if(contentType.startsWith(IMAGE_CONTENT_FILE)){
            return new ImageFileType(kujonFile);
        }else if(contentType.startsWith(VIDEO_CONTENT_FILE)){
            return new VideoFileType(kujonFile);
        }else if(contentType.startsWith(AUDIO_CONTENT_FILE)) {
            return new AudioFileType(kujonFile);
        }else if(contentType.contains(PDF_FILE_CONENT)) {
            return new PdfFileType(kujonFile);
        }else if(contentType.contains(DOC_FILE_CONTENT)) {
            return new DocFileType(kujonFile);
        }else if(contentType.contains(PPT_FILE_CONTENT)) {
            return new PptFileType(kujonFile);
        }if(contentType.contains(XLS_FILE_CONTENT)) {
            return new XlsFileType(kujonFile);
        }else {
            return new NormalFileType(kujonFile);
        }
    }

    public static List<FileDTO> createListOfDTOFiles(List<KujonFile> kujonFiles){
        List<FileDTO> fileDTOs = new ArrayList<>();
        for(KujonFile kujonFile:kujonFiles){
            fileDTOs.add(createFileDto(kujonFile));
        }
        return fileDTOs;
    }
}
