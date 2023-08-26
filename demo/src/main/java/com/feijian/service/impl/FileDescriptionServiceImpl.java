package com.feijian.service.impl;

import com.feijian.dao.projectDetail.FileDescriptionRepository;
import com.feijian.domain.FileDescription;
import com.feijian.domain.Project;
import com.feijian.service.FileDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FileDescriptionServiceImpl implements FileDescriptionService {
    FileDescriptionRepository fileDao;
    @Autowired
    public FileDescriptionServiceImpl(FileDescriptionRepository fileDao) {
        this.fileDao = fileDao;
    }

    @Override
    public FileDescription get(int id) {
        return fileDao.getOne(id);
    }

    @Override
    public FileDescription save(FileDescription system) {
        fileDao.save(system);
        return system;
    }

    @Override
    public FileDescription update(FileDescription fileDescription) {
        fileDao.save(fileDescription);
        return fileDescription;
    }

    @Override
    public List<FileDescription> getFilesByFileName(String fileName) {
        return fileDao.getFileDescriptionsByFileNameLike(fileName);
    }

    @Override
    public List<FileDescription> getFilesByFileType(String fileType) {
        return fileDao.getFileDescriptionsByFileTypeLike(fileType);
    }

    @Override
    public Page<FileDescription> getFilesByProject(Project project, Pageable pageable) {
        return fileDao.getFileDescriptionsByProject(project,pageable);
    }


    @Override
    public List<FileDescription> getFileByKey(String key) {
        Set<FileDescription> fileDesc = new HashSet<>();
        fileDesc.addAll(getFilesByFileName(key));
        fileDesc.addAll(getFilesByFileType(key));
        fileDesc.addAll(fileDao.getFileDescriptionsByDetailLike(key));
        return new ArrayList<>(fileDesc);
    }

    @Override
    public File getFile(FileDescription fileDescription) {
        String path = fileDescription.getEntirety();
        return new File(path);
    }


    @Override
    public void saveFile(FileDescription fileDescription, MultipartFile file) throws IOException {
        File file1 = new File(fileDescription.getEntirety());
        file.transferTo(file1);
    }

    @Override
    public void delete(FileDescription fileDescription) {
        fileDao.delete(fileDescription);
    }
}
