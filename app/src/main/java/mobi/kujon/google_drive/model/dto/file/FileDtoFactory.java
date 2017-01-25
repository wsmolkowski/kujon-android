package mobi.kujon.google_drive.model.dto.file;

import java.util.ArrayList;
import java.util.List;

import mobi.kujon.google_drive.model.KujonFile;

/**
 *
 */

public class FileDtoFactory {

    public static FileDTO createFileDto(KujonFile kujonFile){
        switch (kujonFile.contentType){
            default:
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
