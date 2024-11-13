package com.backend.elearning.domain.referencefile;

import com.backend.elearning.reference.Reference;
import com.backend.elearning.reference.ReferenceRepository;
import com.backend.elearning.reference.ReferenceVM;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReferenceFileServiceImpl implements ReferenceFileService {

    private final ReferenceFileRepo referenceFileRepo;

    private final ReferenceRepository referenceRepository;

    public ReferenceFileServiceImpl(ReferenceFileRepo referenceFileRepo, ReferenceRepository referenceRepository) {
        this.referenceFileRepo = referenceFileRepo;
        this.referenceRepository = referenceRepository;
    }

    @Override
    public ReferenceFileVM create(ReferenceFilePostVM referenceFilePostVM) {
        Reference reference = referenceRepository.findById(referenceFilePostVM.referenceId()).orElseThrow();
        ReferenceFile referenceFile = ReferenceFile.builder()
                .fileName(referenceFilePostVM.fileName())
                .fileUrl(referenceFilePostVM.fileUrl())
                .reference(reference)
                .build();
        ReferenceFile savedReferenceFile = referenceFileRepo.saveAndFlush(referenceFile);
        return ReferenceFileVM.fromModel(savedReferenceFile);
    }

    @Override
    public List<ReferenceFileVM> getByReferenceId(Long referenceId) {
        List<ReferenceFile> referenceFiles = referenceFileRepo.findByReferenceId(referenceId);
        return referenceFiles.stream().map(referenceFile -> ReferenceFileVM.fromModel(referenceFile)).toList();
    }
}
