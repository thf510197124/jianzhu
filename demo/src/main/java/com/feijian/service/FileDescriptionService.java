package com.feijian.service;

import com.feijian.domain.FileDescription;
import com.feijian.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileDescriptionService {
    public FileDescription get(int id);
    public FileDescription save(FileDescription system);
    public FileDescription update(FileDescription fileDescription);
    public List<FileDescription> getFilesByFileName(String fileName);
    public List<FileDescription> getFilesByFileType(String fileType);
    public Page<FileDescription> getFilesByProject(Project project, Pageable pageable);
    public List<FileDescription> getFileByKey(String key);
    //根据文件描述，获取文件
    public File getFile(FileDescription fileDescription);
    //根据文件描述的地址和名称，保存文件

    /**
     *
     * @param fileDescription 文件描述，包含文件的地址，其中使用的就是文件的地址
     * @param file 需要保存的文件
     * @throws IOException 文件夹不存在
     */
    public void saveFile(FileDescription fileDescription, MultipartFile file) throws IOException;
    public void delete(FileDescription fileDescription);
}
