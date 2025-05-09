package com.backend.elearning.domain.classroom;

import com.backend.elearning.domain.common.Event;
import com.backend.elearning.domain.common.EventType;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.excercise.Exercise;
import com.backend.elearning.domain.excercise.ExerciseGetVM;
import com.backend.elearning.domain.exerciseFile.ExerciseFileService;
import com.backend.elearning.domain.exerciseFile.ExerciseFileVM;
import com.backend.elearning.domain.meeting.Meeting;
import com.backend.elearning.domain.meeting.MeetingGetVM;
import com.backend.elearning.domain.referencefile.ReferenceFileService;
import com.backend.elearning.domain.referencefile.ReferenceFileVM;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.domain.reference.Reference;
import com.backend.elearning.domain.reference.ReferenceGetVM;
import com.backend.elearning.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.backend.elearning.utils.DateTimeUtils.convertLocalDateTimeToString;

@Service
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final CourseRepository courseRepository;

    private final ReferenceFileService referenceFileService;

    private final ExerciseFileService exerciseFileService;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository, CourseRepository courseRepository, ReferenceFileService referenceFileService, ExerciseFileService exerciseFileService) {
        this.classroomRepository = classroomRepository;
        this.courseRepository = courseRepository;
        this.referenceFileService = referenceFileService;
        this.exerciseFileService = exerciseFileService;
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
    public ClassroomVM update(ClassroomPostVM classroomPostVM, Long classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow();
        classroom.setName(classroom.getName());
        classroom.setDescription(classroomPostVM.description());
        classroom.setImage(classroomPostVM.image());


        Classroom updatedClassroom = classroomRepository.save(classroom);
        return ClassroomVM.fromModel(updatedClassroom);
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
        classroom = classroomRepository.findByIdExercises(classroom).orElseThrow(() ->
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
            meetingGetVM.setStartTime(convertLocalDateTimeToString(meeting.getStartTime()));
            meetingGetVM.setEndTime(convertLocalDateTimeToString(meeting.getEndTime()));
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

        List<Exercise> exercises = classroom.getExercises();

        for (Exercise exercise : exercises) {
            ExerciseGetVM exerciseGetVM = new ExerciseGetVM();
            exerciseGetVM.setId(exercise.getId());
            exerciseGetVM.setTitle(exercise.getTitle());
            exerciseGetVM.setDescription(exercise.getDescription());
            exerciseGetVM.setDeadline(convertLocalDateTimeToString(exercise.getSubmission_deadline()));
            exerciseGetVM.setType(EventType.exercise);
            exerciseGetVM.setCreatedAt(exercise.getCreatedAt());
            List<ExerciseFileVM> files = exerciseFileService.getByExerciseId(exercise.getId());
            exerciseGetVM.setFiles(files);
            events.add(exerciseGetVM);
        }

        events.sort(Comparator.comparing(Event::getCreatedAt).reversed());
        return events;
    }
}
