package com.backend.elearning.domain.classroom;

import com.backend.elearning.domain.common.Curriculum;
import com.backend.elearning.domain.common.Event;
import com.backend.elearning.domain.common.EventType;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.meeting.Meeting;
import com.backend.elearning.domain.meeting.MeetingGetVM;
import com.backend.elearning.domain.referencefile.ReferenceFile;
import com.backend.elearning.domain.referencefile.ReferenceFileService;
import com.backend.elearning.domain.referencefile.ReferenceFileVM;
import com.backend.elearning.domain.section.Section;
import com.backend.elearning.domain.section.SectionVM;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.reference.Reference;
import com.backend.elearning.reference.ReferenceGetVM;
import com.backend.elearning.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final CourseRepository courseRepository;

    private final ReferenceFileService referenceFileService;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository, CourseRepository courseRepository, ReferenceFileService referenceFileService) {
        this.classroomRepository = classroomRepository;
        this.courseRepository = courseRepository;
        this.referenceFileService = referenceFileService;
    }

    @Override
    public ClassroomVM create(ClassroomPostVM classroomPostVM) {
        Course course = courseRepository.findById(classroomPostVM.courseId()).orElseThrow();
        Classroom classroom = Classroom.builder()
                .name(classroomPostVM.name())
                .description(classroomPostVM.description())
                .image(classroomPostVM.image())
                .course(course)
                .build();
        Classroom savedClassroom = classroomRepository.saveAndFlush(classroom);
        return ClassroomVM.fromModel(savedClassroom);
    }

    @Override
    public List<ClassroomVM> getByCourseId(Long courseId) {
        List<Classroom> classrooms = classroomRepository.findByCourseId(courseId);
        return classrooms.stream().map(ClassroomVM::fromModel).toList();
    }

    @Override
    public ClassroomGetVM getById(Long id) {
        Classroom classroom = classroomRepository.findByIdMeetings(id).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.SECTION_NOT_FOUND, id));
        classroom = classroomRepository.findByIdReferences(classroom).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.SECTION_NOT_FOUND, id));
        List<Event> events = getByClassroom(classroom);
        return ClassroomGetVM.fromModel(classroom, events);
    }

    private List<Event> getByClassroom(Classroom classroom) {
        List<Event> events = new ArrayList<>();

        List<Meeting> meetings = classroom.getMeetings();

        for (Meeting meeting : meetings) {
            MeetingGetVM meetingGetVM = new MeetingGetVM();
            meetingGetVM.setId(meeting.getId());
            meetingGetVM.setCode(meeting.getCode());
            meetingGetVM.setType(EventType.meeting);
            meetingGetVM.setStartTime(meeting.getStartTime().toString());
            meetingGetVM.setEndTime(meeting.getEndTime().toString());
            meetingGetVM.setCreatedAt(meeting.getCreatedAt());
            events.add(meetingGetVM);
        }


        List<Reference> references = classroom.getReferences();

        for (Reference reference : references) {
            ReferenceGetVM referenceGetVM = new ReferenceGetVM();
            referenceGetVM.setId(reference.getId());
            referenceGetVM.setType(EventType.reference);
            referenceGetVM.setDescription(reference.getDescription());
            referenceGetVM.setCreatedAt(reference.getCreatedAt());
            List<ReferenceFileVM> files = referenceFileService.getByReferenceId(reference.getId());
            referenceGetVM.setFiles(files);
            events.add(referenceGetVM);
        }

        events.sort(Comparator.comparing(Event::getCreatedAt).reversed());
        return events;
    }
}
