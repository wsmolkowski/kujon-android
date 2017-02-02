package mobi.kujon.google_drive.model.dto.file;

import java.util.ArrayList;
import java.util.List;

import mobi.kujon.google_drive.model.KujonFile;

/**
 *
 */

public class FileDtoFactory {

    public static FileDTO createFileDto(KujonFile kujonFile){
        String contentType = kujonFile.contentType;

        if(contentType.startsWith("image")){
            return new ImageFileType(kujonFile);
        }else if(contentType.startsWith("video")){
            return new VideoFileType(kujonFile);
        }else if(contentType.startsWith("audio")) {
            return new AudioFileType(kujonFile);
        }else if(contentType.contains("pdf")) {
            return new PdfFileType(kujonFile);
        }else if(contentType.contains("document")) {
            return new DocFileType(kujonFile);
        }else if(contentType.contains("presentation")) {
            return new PptFileType(kujonFile);
        }if(contentType.contains("sheet")) {
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

//
//case msWord = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
//        case msPowerPoint = "application/vnd.openxmlformats-officedocument.presentationml.presentation"
//        case msExcel = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"