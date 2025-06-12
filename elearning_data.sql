--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3 (Debian 16.3-1.pgdg120+1)
-- Dumped by pg_dump version 16.3 (Debian 16.3-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: answer; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.answer (
                               id bigint NOT NULL,
                               created_at timestamp(6) without time zone,
                               updated_at timestamp(6) without time zone,
                               answer_text character varying(255),
                               correct boolean NOT NULL,
                               reason character varying(255),
                               question_id bigint
);


ALTER TABLE public.answer OWNER TO root;

--
-- Name: answer_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.answer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.answer_id_seq OWNER TO root;

--
-- Name: answer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.answer_id_seq OWNED BY public.answer.id;


--
-- Name: cart; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.cart (
                             id bigint NOT NULL,
                             buy_later boolean NOT NULL,
                             course_id bigint,
                             student_id bigint
);


ALTER TABLE public.cart OWNER TO root;

--
-- Name: cart_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.cart_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.cart_id_seq OWNER TO root;

--
-- Name: cart_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.cart_id_seq OWNED BY public.cart.id;


--
-- Name: category; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.category (
                                 id integer NOT NULL,
                                 created_at timestamp(6) without time zone,
                                 updated_at timestamp(6) without time zone,
                                 description character varying(255),
                                 name character varying(40),
                                 publish boolean NOT NULL,
                                 parent_id integer
);


ALTER TABLE public.category OWNER TO root;

--
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.category_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.category_id_seq OWNER TO root;

--
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.category_id_seq OWNED BY public.category.id;


--
-- Name: category_topic; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.category_topic (
                                       topic_id integer NOT NULL,
                                       category_id integer NOT NULL
);


ALTER TABLE public.category_topic OWNER TO root;

--
-- Name: classroom; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.classroom (
                                  id bigint NOT NULL,
                                  description character varying(255),
                                  image character varying(255),
                                  name character varying(255),
                                  course_id bigint
);


ALTER TABLE public.classroom OWNER TO root;

--
-- Name: classroom_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.classroom_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.classroom_id_seq OWNER TO root;

--
-- Name: classroom_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.classroom_id_seq OWNED BY public.classroom.id;


--
-- Name: coupon; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.coupon (
                               id bigint NOT NULL,
                               code character varying(255),
                               discount_percent integer NOT NULL,
                               end_time timestamp(6) without time zone,
                               start_time timestamp(6) without time zone
);


ALTER TABLE public.coupon OWNER TO root;

--
-- Name: coupon_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.coupon_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.coupon_id_seq OWNER TO root;

--
-- Name: coupon_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.coupon_id_seq OWNED BY public.coupon.id;


--
-- Name: course; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.course (
                               id bigint NOT NULL,
                               created_at timestamp(6) without time zone,
                               updated_at timestamp(6) without time zone,
                               description character varying(1000),
                               free boolean NOT NULL,
                               headline character varying(255),
                               image_id character varying(255),
                               level character varying(255),
                               objectives character varying(255)[],
                               price bigint,
                               publish boolean NOT NULL,
                               requirements character varying(255)[],
                               slug character varying(255),
                               target_audiences character varying(255)[],
                               title character varying(60) NOT NULL,
                               category_id integer,
                               topic_id integer,
                               user_id bigint,
                               reason_refused character varying(255),
                               status character varying(255),
                               CONSTRAINT course_level_check CHECK (((level)::text = ANY (ARRAY[('Beginner'::character varying)::text, ('Intermediate'::character varying)::text, ('Expert'::character varying)::text, ('AllLevel'::character varying)::text]))),
    CONSTRAINT course_status_check CHECK (((status)::text = ANY ((ARRAY['PUBLISHED'::character varying, 'UNPUBLISHED'::character varying, 'UNDER_REVIEW'::character varying])::text[])))
);


ALTER TABLE public.course OWNER TO root;

--
-- Name: course_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.course_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.course_id_seq OWNER TO root;

--
-- Name: course_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.course_id_seq OWNED BY public.course.id;


--
-- Name: course_promotion; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.course_promotion (
                                         course_id bigint NOT NULL,
                                         promotion_id bigint NOT NULL
);


ALTER TABLE public.course_promotion OWNER TO root;

--
-- Name: exercise; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.exercise (
                                 id bigint NOT NULL,
                                 created_at timestamp(6) without time zone,
                                 updated_at timestamp(6) without time zone,
                                 description character varying(255),
                                 submission_deadline timestamp(6) without time zone,
                                 title character varying(255),
                                 classroom_id bigint
);


ALTER TABLE public.exercise OWNER TO root;

--
-- Name: exercise_file; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.exercise_file (
                                      id bigint NOT NULL,
                                      file_name character varying(255),
                                      file_url character varying(255),
                                      exercise_id bigint
);


ALTER TABLE public.exercise_file OWNER TO root;

--
-- Name: exercise_file_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.exercise_file_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.exercise_file_id_seq OWNER TO root;

--
-- Name: exercise_file_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.exercise_file_id_seq OWNED BY public.exercise_file.id;


--
-- Name: exercise_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.exercise_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.exercise_id_seq OWNER TO root;

--
-- Name: exercise_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.exercise_id_seq OWNED BY public.exercise.id;


--
-- Name: learning_course; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.learning_course (
                                        id bigint NOT NULL,
                                        course_id bigint,
                                        student_id bigint
);


ALTER TABLE public.learning_course OWNER TO root;

--
-- Name: learning_course_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.learning_course_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.learning_course_id_seq OWNER TO root;

--
-- Name: learning_course_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.learning_course_id_seq OWNED BY public.learning_course.id;


--
-- Name: learning_lecture; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.learning_lecture (
                                         id bigint NOT NULL,
                                         access_time timestamp(6) without time zone,
                                         finished boolean NOT NULL,
                                         watching_second integer NOT NULL,
                                         lecture_id bigint,
                                         student_id bigint
);


ALTER TABLE public.learning_lecture OWNER TO root;

--
-- Name: learning_lecture_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.learning_lecture_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.learning_lecture_id_seq OWNER TO root;

--
-- Name: learning_lecture_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.learning_lecture_id_seq OWNED BY public.learning_lecture.id;


--
-- Name: learning_quiz; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.learning_quiz (
                                      id bigint NOT NULL,
                                      access_time timestamp(6) without time zone,
                                      finished boolean NOT NULL,
                                      quiz_id bigint,
                                      student_id bigint
);


ALTER TABLE public.learning_quiz OWNER TO root;

--
-- Name: learning_quiz_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.learning_quiz_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.learning_quiz_id_seq OWNER TO root;

--
-- Name: learning_quiz_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.learning_quiz_id_seq OWNED BY public.learning_quiz.id;


--
-- Name: lecture; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.lecture (
                                id bigint NOT NULL,
                                created_at timestamp(6) without time zone,
                                updated_at timestamp(6) without time zone,
                                duration integer NOT NULL,
                                lecture_details character varying(255),
                                number real NOT NULL,
                                title character varying(80) NOT NULL,
                                video_id character varying(255),
                                section_id bigint
);


ALTER TABLE public.lecture OWNER TO root;

--
-- Name: lecture_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.lecture_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.lecture_id_seq OWNER TO root;

--
-- Name: lecture_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.lecture_id_seq OWNED BY public.lecture.id;


--
-- Name: meeting; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.meeting (
                                id bigint NOT NULL,
                                created_at timestamp(6) without time zone,
                                updated_at timestamp(6) without time zone,
                                code character varying(255),
                                end_time timestamp(6) without time zone,
                                start_time timestamp(6) without time zone,
                                classroom_id bigint
);


ALTER TABLE public.meeting OWNER TO root;

--
-- Name: meeting_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.meeting_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.meeting_id_seq OWNER TO root;

--
-- Name: meeting_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.meeting_id_seq OWNED BY public.meeting.id;


--
-- Name: note; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.note (
                             id bigint NOT NULL,
                             content character varying(255),
                             "time" integer NOT NULL,
                             lecture_id bigint,
                             student_id bigint
);


ALTER TABLE public.note OWNER TO root;

--
-- Name: note_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.note_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.note_id_seq OWNER TO root;

--
-- Name: note_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.note_id_seq OWNED BY public.note.id;


--
-- Name: order_detail; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.order_detail (
                                     id bigint NOT NULL,
                                     price bigint,
                                     course_id bigint,
                                     order_id bigint
);


ALTER TABLE public.order_detail OWNER TO root;

--
-- Name: order_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.order_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.order_detail_id_seq OWNER TO root;

--
-- Name: order_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.order_detail_id_seq OWNED BY public.order_detail.id;


--
-- Name: orders; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.orders (
                               id bigint NOT NULL,
                               created_at timestamp(6) without time zone,
                               status character varying(255),
                               coupon_id bigint,
                               student_id bigint,
                               reason_failed character varying(255),
                               CONSTRAINT orders_status_check CHECK (((status)::text = ANY (ARRAY[('PENDING'::character varying)::text, ('SUCCESS'::character varying)::text, ('FAILURE'::character varying)::text])))
);


ALTER TABLE public.orders OWNER TO root;

--
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.orders_id_seq OWNER TO root;

--
-- Name: orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;


--
-- Name: payment; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.payment (
                                id bigint NOT NULL,
                                amount bigint NOT NULL,
                                bank_code character varying(255),
                                bank_tran_no character varying(255),
                                cart_type character varying(255),
                                pay_date timestamp(6) without time zone,
                                order_id bigint,
                                card_type character varying(255)
);


ALTER TABLE public.payment OWNER TO root;

--
-- Name: payment_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.payment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.payment_id_seq OWNER TO root;

--
-- Name: payment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.payment_id_seq OWNED BY public.payment.id;


--
-- Name: promotion; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.promotion (
                                  id bigint NOT NULL,
                                  discount_percent integer NOT NULL,
                                  end_time timestamp(6) without time zone NOT NULL,
                                  name character varying(255),
                                  start_time timestamp(6) without time zone NOT NULL
);


ALTER TABLE public.promotion OWNER TO root;

--
-- Name: promotion_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.promotion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.promotion_id_seq OWNER TO root;

--
-- Name: promotion_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.promotion_id_seq OWNED BY public.promotion.id;


--
-- Name: question; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.question (
                                 id bigint NOT NULL,
                                 created_at timestamp(6) without time zone,
                                 updated_at timestamp(6) without time zone,
                                 title character varying(255),
                                 quiz_id bigint
);


ALTER TABLE public.question OWNER TO root;

--
-- Name: question_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.question_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.question_id_seq OWNER TO root;

--
-- Name: question_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.question_id_seq OWNED BY public.question.id;


--
-- Name: question_lecture; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.question_lecture (
                                         id bigint NOT NULL,
                                         created_at timestamp(6) without time zone,
                                         updated_at timestamp(6) without time zone,
                                         description character varying(255),
                                         title character varying(255),
                                         lecture_id bigint,
                                         student_id bigint
);


ALTER TABLE public.question_lecture OWNER TO root;

--
-- Name: question_lecture_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.question_lecture_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.question_lecture_id_seq OWNER TO root;

--
-- Name: question_lecture_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.question_lecture_id_seq OWNED BY public.question_lecture.id;


--
-- Name: quiz; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.quiz (
                             id bigint NOT NULL,
                             created_at timestamp(6) without time zone,
                             updated_at timestamp(6) without time zone,
                             description character varying(255),
                             number real NOT NULL,
                             title character varying(255),
                             section_id bigint
);


ALTER TABLE public.quiz OWNER TO root;

--
-- Name: quiz_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.quiz_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.quiz_id_seq OWNER TO root;

--
-- Name: quiz_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.quiz_id_seq OWNED BY public.quiz.id;


--
-- Name: reference; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.reference (
                                  id bigint NOT NULL,
                                  created_at timestamp(6) without time zone,
                                  updated_at timestamp(6) without time zone,
                                  description character varying(255),
                                  classroom_id bigint
);


ALTER TABLE public.reference OWNER TO root;

--
-- Name: reference_file; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.reference_file (
                                       id bigint NOT NULL,
                                       file_name character varying(255),
                                       file_url character varying(255),
                                       reference_id bigint
);


ALTER TABLE public.reference_file OWNER TO root;

--
-- Name: reference_file_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.reference_file_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.reference_file_id_seq OWNER TO root;

--
-- Name: reference_file_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.reference_file_id_seq OWNED BY public.reference_file.id;


--
-- Name: reference_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.reference_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.reference_id_seq OWNER TO root;

--
-- Name: reference_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.reference_id_seq OWNED BY public.reference.id;


--
-- Name: review; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.review (
                               id bigint NOT NULL,
                               content character varying(255),
                               created_at timestamp(6) without time zone,
                               rating_star integer NOT NULL,
                               status character varying(255) NOT NULL,
                               updated_at timestamp(6) without time zone,
                               course_id bigint,
                               student_id bigint,
                               reason_refused character varying(255)
);


ALTER TABLE public.review OWNER TO root;

--
-- Name: review_classroom; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.review_classroom (
                                         id bigint NOT NULL,
                                         content character varying(255),
                                         created_at timestamp(6) without time zone,
                                         rating_star integer NOT NULL,
                                         status boolean NOT NULL,
                                         updated_at timestamp(6) without time zone,
                                         classroom_id bigint,
                                         student_id bigint
);


ALTER TABLE public.review_classroom OWNER TO root;

--
-- Name: review_classroom_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.review_classroom_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.review_classroom_id_seq OWNER TO root;

--
-- Name: review_classroom_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.review_classroom_id_seq OWNED BY public.review_classroom.id;


--
-- Name: review_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.review_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.review_id_seq OWNER TO root;

--
-- Name: review_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.review_id_seq OWNED BY public.review.id;


--
-- Name: section; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.section (
                                id bigint NOT NULL,
                                created_at timestamp(6) without time zone,
                                updated_at timestamp(6) without time zone,
                                number real NOT NULL,
                                objective character varying(255),
                                title character varying(255),
                                course_id bigint
);


ALTER TABLE public.section OWNER TO root;

--
-- Name: section_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.section_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.section_id_seq OWNER TO root;

--
-- Name: section_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.section_id_seq OWNED BY public.section.id;


--
-- Name: student; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.student (
                                id bigint NOT NULL,
                                created_at timestamp(6) without time zone,
                                updated_at timestamp(6) without time zone,
                                active boolean NOT NULL,
                                date_of_birth date,
                                email character varying(50),
                                first_name character varying(20),
                                gender character varying(255),
                                last_name character varying(20),
                                password character varying(255),
                                photo character varying(255),
                                verification_code character varying(255),
                                verification_expiration timestamp(6) without time zone,
                                CONSTRAINT student_gender_check CHECK (((gender)::text = ANY (ARRAY[('MALE'::character varying)::text, ('FEMALE'::character varying)::text])))
);


ALTER TABLE public.student OWNER TO root;

--
-- Name: student_answer; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.student_answer (
                                       id bigint NOT NULL,
                                       created_at timestamp(6) without time zone,
                                       updated_at timestamp(6) without time zone,
                                       content character varying(255),
                                       question_lecture_id bigint,
                                       student_id bigint
);


ALTER TABLE public.student_answer OWNER TO root;

--
-- Name: student_answer_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.student_answer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.student_answer_id_seq OWNER TO root;

--
-- Name: student_answer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.student_answer_id_seq OWNED BY public.student_answer.id;


--
-- Name: student_exercise; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.student_exercise (
                                         id bigint NOT NULL,
                                         file_name character varying(255),
                                         file_url character varying(255),
                                         submitted boolean,
                                         submitted_time timestamp(6) without time zone,
                                         exercise_id bigint,
                                         student_id bigint
);


ALTER TABLE public.student_exercise OWNER TO root;

--
-- Name: student_exercise_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.student_exercise_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.student_exercise_id_seq OWNER TO root;

--
-- Name: student_exercise_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.student_exercise_id_seq OWNED BY public.student_exercise.id;


--
-- Name: student_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.student_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.student_id_seq OWNER TO root;

--
-- Name: student_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.student_id_seq OWNED BY public.student.id;


--
-- Name: topic; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.topic (
                              id integer NOT NULL,
                              created_at timestamp(6) without time zone,
                              updated_at timestamp(6) without time zone,
                              description character varying(255),
                              name character varying(255),
                              publish boolean NOT NULL
);


ALTER TABLE public.topic OWNER TO root;

--
-- Name: topic_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.topic_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.topic_id_seq OWNER TO root;

--
-- Name: topic_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.topic_id_seq OWNED BY public.topic.id;


--
-- Name: user; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public."user" (
                               id bigint NOT NULL,
                               created_at timestamp(6) without time zone,
                               updated_at timestamp(6) without time zone,
                               active boolean NOT NULL,
                               date_of_birth date,
                               email character varying(50),
                               first_name character varying(20),
                               gender character varying(255),
                               headline character varying(50),
                               last_name character varying(20),
                               password character varying(255),
                               photo character varying(255),
                               role character varying(255),
                               CONSTRAINT user_gender_check CHECK (((gender)::text = ANY (ARRAY[('MALE'::character varying)::text, ('FEMALE'::character varying)::text]))),
    CONSTRAINT user_role_check CHECK (((role)::text = ANY (ARRAY[('ROLE_ADMIN'::character varying)::text, ('ROLE_INSTRUCTOR'::character varying)::text, ('ROLE_STUDENT'::character varying)::text])))
);


ALTER TABLE public."user" OWNER TO root;

--
-- Name: user_answer; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.user_answer (
                                    id bigint NOT NULL,
                                    created_at timestamp(6) without time zone,
                                    updated_at timestamp(6) without time zone,
                                    content character varying(255),
                                    question_lecture_id bigint,
                                    user_id bigint
);


ALTER TABLE public.user_answer OWNER TO root;

--
-- Name: user_answer_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.user_answer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_answer_id_seq OWNER TO root;

--
-- Name: user_answer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.user_answer_id_seq OWNED BY public.user_answer.id;


--
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_id_seq OWNER TO root;

--
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.user_id_seq OWNED BY public."user".id;


--
-- Name: answer id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.answer ALTER COLUMN id SET DEFAULT nextval('public.answer_id_seq'::regclass);


--
-- Name: cart id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.cart ALTER COLUMN id SET DEFAULT nextval('public.cart_id_seq'::regclass);


--
-- Name: category id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.category ALTER COLUMN id SET DEFAULT nextval('public.category_id_seq'::regclass);


--
-- Name: classroom id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.classroom ALTER COLUMN id SET DEFAULT nextval('public.classroom_id_seq'::regclass);


--
-- Name: coupon id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.coupon ALTER COLUMN id SET DEFAULT nextval('public.coupon_id_seq'::regclass);


--
-- Name: course id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.course ALTER COLUMN id SET DEFAULT nextval('public.course_id_seq'::regclass);


--
-- Name: exercise id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.exercise ALTER COLUMN id SET DEFAULT nextval('public.exercise_id_seq'::regclass);


--
-- Name: exercise_file id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.exercise_file ALTER COLUMN id SET DEFAULT nextval('public.exercise_file_id_seq'::regclass);


--
-- Name: learning_course id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.learning_course ALTER COLUMN id SET DEFAULT nextval('public.learning_course_id_seq'::regclass);


--
-- Name: learning_lecture id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.learning_lecture ALTER COLUMN id SET DEFAULT nextval('public.learning_lecture_id_seq'::regclass);


--
-- Name: learning_quiz id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.learning_quiz ALTER COLUMN id SET DEFAULT nextval('public.learning_quiz_id_seq'::regclass);


--
-- Name: lecture id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.lecture ALTER COLUMN id SET DEFAULT nextval('public.lecture_id_seq'::regclass);


--
-- Name: meeting id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.meeting ALTER COLUMN id SET DEFAULT nextval('public.meeting_id_seq'::regclass);


--
-- Name: note id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.note ALTER COLUMN id SET DEFAULT nextval('public.note_id_seq'::regclass);


--
-- Name: order_detail id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.order_detail ALTER COLUMN id SET DEFAULT nextval('public.order_detail_id_seq'::regclass);


--
-- Name: orders id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);


--
-- Name: payment id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.payment ALTER COLUMN id SET DEFAULT nextval('public.payment_id_seq'::regclass);


--
-- Name: promotion id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.promotion ALTER COLUMN id SET DEFAULT nextval('public.promotion_id_seq'::regclass);


--
-- Name: question id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.question ALTER COLUMN id SET DEFAULT nextval('public.question_id_seq'::regclass);


--
-- Name: question_lecture id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.question_lecture ALTER COLUMN id SET DEFAULT nextval('public.question_lecture_id_seq'::regclass);


--
-- Name: quiz id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.quiz ALTER COLUMN id SET DEFAULT nextval('public.quiz_id_seq'::regclass);


--
-- Name: reference id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.reference ALTER COLUMN id SET DEFAULT nextval('public.reference_id_seq'::regclass);


--
-- Name: reference_file id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.reference_file ALTER COLUMN id SET DEFAULT nextval('public.reference_file_id_seq'::regclass);


--
-- Name: review id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.review ALTER COLUMN id SET DEFAULT nextval('public.review_id_seq'::regclass);


--
-- Name: review_classroom id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.review_classroom ALTER COLUMN id SET DEFAULT nextval('public.review_classroom_id_seq'::regclass);


--
-- Name: section id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.section ALTER COLUMN id SET DEFAULT nextval('public.section_id_seq'::regclass);


--
-- Name: student id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.student ALTER COLUMN id SET DEFAULT nextval('public.student_id_seq'::regclass);


--
-- Name: student_answer id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.student_answer ALTER COLUMN id SET DEFAULT nextval('public.student_answer_id_seq'::regclass);


--
-- Name: student_exercise id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.student_exercise ALTER COLUMN id SET DEFAULT nextval('public.student_exercise_id_seq'::regclass);


--
-- Name: topic id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.topic ALTER COLUMN id SET DEFAULT nextval('public.topic_id_seq'::regclass);


--
-- Name: user id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public."user" ALTER COLUMN id SET DEFAULT nextval('public.user_id_seq'::regclass);


--
-- Name: user_answer id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.user_answer ALTER COLUMN id SET DEFAULT nextval('public.user_answer_id_seq'::regclass);


--
-- Data for Name: answer; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.answer (id, created_at, updated_at, answer_text, correct, reason, question_id) FROM stdin;
2       \N      \N      <p>no</p>       f       \N      1
5       \N      \N      <p>no</p>       f       \N      2
4       \N      \N      <p>yes</p>      t       \N      2
7       \N      \N      <p>no</p>       f               7
6       \N      \N      <p>yes</p>      t               7
8       \N      \N      <p>a</p>        f               8
9       \N      \N      <p>b</p>        t               8
10      \N      \N      <p>aaaa</p>     f               9
11      \N      \N      <p>bbbb</p>     f               9
12      \N      \N      <p>a</p>        t               10
13      \N      \N      <p>b</p>        f               10
1       \N      \N      <p>yess</p>     t       \N      1
\.


--
-- Data for Name: cart; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.cart (id, buy_later, course_id, student_id) FROM stdin;
6       t       20      1
9       f       2       16
\.


--
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.category (id, created_at, updated_at, description, name, publish, parent_id) FROM stdin;
5       2024-06-30 17:52:30.004737      2024-06-30 17:52:30.004737      string  Kinh doanh      t       \N
6       2024-06-30 17:52:43.134723      2024-06-30 17:52:43.134723      string  Tài chính & kế toán     t       \N
7       2024-06-30 17:52:59.628779      2024-06-30 17:52:59.628779      string  Phát triển cá nhân      t       \N
8       2024-06-30 17:53:17.740821      2024-06-30 17:53:17.740821      string  Marketing       t       \N
9       2024-06-30 17:53:38.165929      2024-06-30 17:53:38.165929      string  Sức khỏe & thể dục      t       \N
10      2024-06-30 17:56:07.489301      2024-06-30 17:56:07.489301      string  Chứng chỉ CNTT  t       1
11      2024-06-30 17:56:21.434562      2024-06-30 17:56:21.434562      string  Mạng và bảo mật t       1
12      2024-06-30 17:56:38.36843       2024-06-30 17:56:38.36843       string  Phần cứng       t       1
13      2024-06-30 17:56:49.911112      2024-06-30 17:56:49.911112      string  Hệ điều hành và máy chủ t       1
14      2024-06-30 17:58:15.29362       2024-06-30 17:58:15.29362       string  Phát triển Web  t       4
15      2024-06-30 17:58:23.477031      2024-06-30 17:58:23.477031      string  Khoa học dữ liệu        t       4
16      2024-06-30 17:58:46.399877      2024-06-30 17:58:46.399877      string  Phát triển ứng dụng di động     t      4
17      2024-06-30 17:59:03.374789      2024-06-30 17:59:03.374789      string  Ngôn ngữ lập trình      t       4
2       2024-07-12 07:47:23.371284      2024-07-12 07:47:23.371284      string  Thiết kế        t       \N
18      2024-06-30 17:59:03.374789      2024-06-30 17:59:03.374789      desc    Năng xuất văn phòng     t       \N
3       2024-06-30 17:52:00.536243      2024-08-08 06:27:33.609457      string  Âm nhạc t       \N
22      2024-08-09 19:40:03.790409      2024-08-09 19:40:03.790409      af      test    t       18
4       2024-06-30 17:52:18.640034      2024-08-09 20:07:21.276149      string  Phát triển      t       \N
1       2024-07-12 07:46:58.778732      2024-08-10 08:28:15.426982      desc    CNTT & phần mềm t       \N
\.


--
-- Data for Name: category_topic; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.category_topic (topic_id, category_id) FROM stdin;
2       14
3       14
4       14
5       14
20      14
1       14
19      14
21      22
\.


--
-- Data for Name: classroom; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.classroom (id, description, image, name, course_id) FROM stdin;
\.


--
-- Data for Name: coupon; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.coupon (id, code, discount_percent, end_time, start_time) FROM stdin;
1       asdfa   15      2024-07-29 10:30:43     2024-07-24 10:30:43
2       DPjNoM  10      2024-07-31 22:13:29     2024-07-24 22:13:08
4       asdfaaaa        12      2024-07-25 10:30:43     2024-07-24 10:30:43
3       FASFASDFA       12      2024-08-10 10:30:43     2024-08-07 10:30:43
5       ff224aafa       24      2024-08-30 06:34:08     2024-08-08 06:33:58
6       y6iodC  12      2024-08-30 06:35:56     2024-08-08 06:35:40
7       JC9WUAaaaa      15      2024-08-30 19:42:51     2024-08-09 19:42:47
\.


--
-- Data for Name: course; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.course (id, created_at, updated_at, description, free, headline, image_id, level, objectives, price, publish, requirements, slug, target_audiences, title, category_id, topic_id, user_id, reason_refused, status) FROM stdin;
1       2024-07-12 07:55:18.830525      2024-08-09 10:23:09.676672      <p><strong>NOTES:</strong>&nbsp;This course has been updated to&nbsp;<strong>Java 21</strong>,&nbsp;<strong>Spring&nbsp;Boot 3.2.0</strong>&nbsp;and&nbsp;<strong>Spring Security 6.2.0</strong>.</p><p>Welcome to&nbsp;<strong>"Java Spring Boot E-Commerce Ultimate Course"</strong>&nbsp;instructed by me, Nam&nbsp;Ha Minh - a&nbsp;<strong>certified Java developer</strong>&nbsp;who have been programming with Java technologies for more than 19 years.</p>     f       My course is number one http://res.cloudinary.com/di6h4mtfa/image/upload/v1721187281/435adb84-806f-40e9-8501-74a7e3912c4d.jpg   Beginner        {"fullstact de","fullstact dev","fullstact dev","fullstact dev"}        199000  t       {"Have laptop and wifi","Have laptop and wifi","Have laptop and wifi","Have laptop and wifi 2"} my-course-is-number-one {all}   Viết ứng dụng bán hàng với Java Springboot/API và Angular      14       1       1       \N      PUBLISHED
2       2024-07-12 21:22:43.114577      2024-08-09 14:26:58.117982              f       My course is number one http://res.cloudinary.com/di6h4mtfa/image/upload/v1723188419/b0ef9e5a-8673-45bd-a876-bd9c5b951bd4.jpg   Intermediate    \N     200001   t       \N      my-course-2     \N      [NEW] Ultimate AWS Certified Cloud Practitioner CLF-C02 10      1      1\N      PUBLISHED
3       2024-07-12 21:22:48.409202      2024-08-09 14:30:03.703438              f       My course is number one http://res.cloudinary.com/di6h4mtfa/image/upload/v1723188604/eb837ba6-dfa3-4c52-9ce9-32d09fd59475.jpg   Expert  \N      200000 t\N      my-course-3     \N      Javascript cho người mới bắt đầu        14      2       1       \N      PUBLISHED
4       2024-07-12 21:22:53.330093      2024-08-09 14:43:21.635248              f       My course is number one http://res.cloudinary.com/di6h4mtfa/image/upload/v1723188781/6749de46-6ece-44b5-b219-0009c32be57b.jpg   Intermediate    \N     200000   t       \N      my-course-4     \N      Khóa học Javascript Chuyên Sâu  14      2       1       \N      PUBLISHED
5       2024-07-12 21:23:00.441659      2024-08-09 14:44:33.883085              f       My course is number one http://res.cloudinary.com/di6h4mtfa/image/upload/v1723189471/97b1b6b6-e85f-4c1c-bc78-cd4bc2b49d13.jpg   Beginner        \N     200000   t       \N      my-course-5     \N      The Complete JavaScript Course 2024: From Zero to Expert!       14     21       \N      PUBLISHED
6       2024-07-12 21:23:05.78856       2024-08-09 14:47:10.916816              f       My course is number one http://res.cloudinary.com/di6h4mtfa/image/upload/v1723189632/f3df0c86-504c-46e4-8f8d-102adcf7ee44.jpg   Beginner        \N     200000   t       \N      my-course-6     \N      React Pro TypeScript - Thực Hành Dự Án Portfolio        14      1      1\N      PUBLISHED
8       2024-07-12 21:23:05.78856       2024-08-09 22:30:14.178076              f       best java course        http://res.cloudinary.com/di6h4mtfa/image/upload/v1723096734/298c18bb-97ac-48f2-af7e-8478dda453ec.jpg   Beginner        {test} 200000   t       {}      NEW-Master-Microservices-with-SpringBoot,Docker,Kubernetes      {fafaaf}        [NEW] Master Microservices with SpringBoot,Docker,Kubernetes    14      1       5       \N      PUBLISHED
9       2024-08-09 14:47:48.163605      2024-08-09 14:49:25.698098              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723189767/0a098334-1412-42fa-a135-8df87c4c7777.jpg   Beginner        \N      200000  t      \N       Lập-trình-react-js-chuyên-sâu-với-React-Hooks-and-Bootstrap     \N      Lập trình react js chuyên sâu với React Hooks and Bootstrap     14      3       1       \N      PUBLISHED
10      2024-08-09 14:50:12.737384      2024-08-09 14:52:08.632142              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723189912/69d8ad0a-7013-4f73-91a8-5e6540434c4d.jpg   AllLevel        \N      200002  t      \N       React-,Redux,Hooks,-TypeScript,Node.JS,-MongoDB-(MERN-stack)    \N      React ,Redux,Hooks, TypeScript,Node.JS, MongoDB (MERN stack)    14      3       1       \N      PUBLISHED
11      2024-08-09 14:52:56.047632      2024-08-09 14:54:05.703262              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723190047/6b7ba15a-d650-4065-9246-d169d2441f51.jpg   Beginner        \N      200000  f      \N       Khóa-học-ReactJS-từ-cơ-bản-đến-nâng-cao \N      Khóa học ReactJS từ cơ bản đến nâng cao 14      3       1      \N       PUBLISHED
12      2024-08-09 14:55:04.117978      2024-08-09 14:56:00.242447              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723190161/4244badb-0415-486c-b1be-75e68a5b85d2.jpg   AllLevel        \N      200000  t      \N       Modern-React-with-Redux-[2024-Update]   \N      Modern React with Redux [2024 Update]   14      3       1      \N       PUBLISHED
    13      2024-08-09 14:56:51.103101      2024-08-09 14:57:58.283964              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723190279/d8785689-2624-4251-92d4-de36c3b332b8.jpg   AllLevel        \N      200000  t      \N       HTML/CSS-cho-người-mới-bắt-đầu-2023     \N      HTML/CSS cho người mới bắt đầu 2023     14      4       1      \N       PUBLISHED
    26      2024-08-09 15:43:53.03794       2024-08-09 15:44:53.279559              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723193094/0ea27fcf-8585-420c-93bd-368cec1b8a90.jpg   Beginner        \N      200000  f      \N       Học-Java-Functional-Programming-với-Lambdas-&-Stream  \N      Học Java Functional Programming với Lambdas & Stream  14      1       2       \N      PUBLISHED
    14      2024-08-09 14:59:05.752056      2024-08-09 14:59:25.380775              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723190366/cc773a17-1d22-472b-9c7e-7ecaf9f37d80.jpg   AllLevel        \N      200000  t      \N       HTML-CSS-căn-bản-dành-cho-người-mới-bắt-đầu-lập-trình-web       \N      HTML CSS căn bản dành cho người mới bắt đầu lập trình web       14      4       1       \N      PUBLISHED
    15      2024-08-09 15:00:01.752091      2024-08-09 15:01:40.747369              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723190501/f2afca90-0c3d-456c-b45d-dcb6278566e8.jpg   AllLevel        \N      200000  t      \N       Build-Responsive-Real-World-Websites-with-HTML-and-CSS  \N      Build Responsive Real-World Websites with HTML and CSS  14      4       1       \N      PUBLISHED
16      2024-08-09 15:02:03.649527      2024-08-09 15:03:21.225141              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723190602/f5dfe61a-1891-4293-8f57-5f782973860b.jpg   AllLevel        \N      200000  t      \N       HTML-and-CSS-for-Beginners---Build-a-Website-&-Launch-ONLINE    \N      HTML and CSS for Beginners - Build a Website & Launch ONLINE    14      4       1       \N      PUBLISHED
17      2024-08-09 15:03:41.235704      2024-08-09 15:04:43.911357              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723190685/f8fd968f-71f3-4e64-af31-e8036ca403bb.jpg   AllLevel        \N      200000  t      \N       Web-Design-for-Beginners:-Real-World-Coding-in-HTML-&-CSS       \N      Web Design for Beginners: Real World Coding in HTML & CSS       14      4       1       \N      PUBLISHED
18      2024-08-09 15:10:18.342138      2024-08-09 15:24:54.365135              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723191895/76149b58-ac0d-470f-9a63-2a923b9cb626.jpg   AllLevel        \N      199999  f      \N       Angular---The-Complete-Guide-(2024-Edition)     \N      Angular - The Complete Guide (2024 Edition)     14     19       5       \N      PUBLISHED
19      2024-08-09 15:25:16.137376      2024-08-09 15:27:22.050035              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723192043/9419d51b-b02e-4070-86fc-f08cc7807768.jpg   AllLevel        \N      100000  f      \N       The-Complete-Angular-Course:-Beginner-to-Advanced       \N      The Complete Angular Course: Beginner to Advanced       14      19      5       \N      PUBLISHED
20      2024-08-09 15:27:56.112761      2024-08-09 15:28:41.650219              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723192122/a3009e1a-6dee-4dbe-ad07-a01da751fff0.jpg   AllLevel        \N      200000  f      \N       Build-an-app-with-ASPNET-Core-and-Angular-from-scratch  \N      Build an app with ASPNET Core and Angular from scratch  14      19      5       \N      PUBLISHED
21      2024-08-09 15:29:08.8602        2024-08-09 15:29:58.637071              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723192199/06adb680-12f3-4784-9ad4-584d1c6375d3.jpg   AllLevel        \N      200000  f      \N       Angular-Crash-Course-for-Busy-Developers        \N      Angular Crash Course for Busy Developers        14     19       5       \N      PUBLISHED
22      2024-08-09 15:37:15.288387      2024-08-09 15:38:03.529371              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723192684/4e169094-2dcf-4969-86bc-222263ca8997.jpg   Expert  \N      200000  f       \N     Go-Java-Full-Stack-with-Spring-Boot-and-Angular  \N      Go Java Full Stack with Spring Boot and Angular 14      19     5\N      PUBLISHED
23      2024-08-09 15:39:47.713903      2024-08-09 15:41:07.818285              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723192868/3286f196-be01-4372-9e57-750163befe2d.jpg   Beginner        \N      200000  f      \N       Khóa-học-Java-Online-Tiếng-Việt-toàn-tập-(VietJack)     \N      Khóa học Java Online Tiếng Việt toàn tập (VietJack)     14      1       2       \N      PUBLISHED
24      2024-08-09 15:41:37.775374      2024-08-09 15:42:27.829601              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723192949/733ac3a4-7bcd-4698-a6a6-287868f86b14.jpg   Intermediate    \N      200000  f      \N       Lập-trình-Java-cho-người-mới-học        \N      Lập trình Java cho người mới học        14      1       2      \N       PUBLISHED
25      2024-08-09 15:42:42.322122      2024-08-09 15:43:34.089683              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723193015/6c54aca0-c1ce-4ab2-8b4f-77bd60910321.jpg   Intermediate    \N      200000  f      \N       Lập-trình-với-Java-cho-người-mới-bắt-đầu-2023   \N      Lập trình với Java cho người mới bắt đầu 2023   14     12       \N      PUBLISHED
27      2024-08-09 15:45:22.050676      2024-08-09 15:46:14.776342              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723193176/c9f07b03-0b25-454e-b4bc-0b4dd4f02edd.jpg   AllLevel        \N      200000  t      \N       Java-17-Masterclass:-Start-Coding-in-2024       \N      Java 17 Masterclass: Start Coding in 2024       14     12       \N      PUBLISHED
28      2024-08-09 15:47:17.594957      2024-08-09 15:48:18.07953               f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723193299/cffc6c1c-2616-4492-bb71-2b1afe8e968b.jpg   Beginner        \N      200000  t      \N       Next.js-14-&-React---The-Complete-Guide \N      Next.js 14 & React - The Complete Guide 14      20      8      \N       PUBLISHED
29      2024-08-09 15:48:33.950396      2024-08-09 15:49:24.79852               f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723193366/7414ac53-139e-47b5-9c9a-ce7ef390e293.jpg   Intermediate    \N      200000  t      \N       Next-JS:-The-Complete-Developer's-Guide \N      Next JS: The Complete Developer's Guide 14      20      8      \N       PUBLISHED
30      2024-08-09 15:49:42.090739      2024-08-09 15:50:37.420997              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723193438/4223e70c-d47d-456b-8213-055dea993885.jpg   Beginner        \N      200000  t      \N       Next.js-Dev-to-Deployment       \N      Next.js Dev to Deployment       14      20      8       \N      PUBLISHED
31      2024-08-09 15:50:54.666622      2024-08-09 15:51:40.441719              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723193501/74239ed8-f875-4d4c-9e6d-4ef0682f9e88.jpg   Beginner        \N      200000  t      \N       Next.js-by-Example      \N      Next.js by Example      14      20      8       \N      PUBLISHED
32      2024-08-09 15:51:55.95283       2024-08-09 15:52:45.914277              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723193567/d4a112db-c178-4774-921a-c232b5a8dbd3.jpg   AllLevel        \N      200000  t      \N       Complete-Next.js-with-React-&-Node---Portfolio-Apps-[2023]      \N      Complete Next.js with React & Node - Portfolio Apps [2023]      14      20      8       \N      PUBLISHED
33      2024-08-09 20:40:08.040211      2024-08-09 20:43:12.864854              f               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723210964/e67a182e-93b9-4fe4-bdd6-667dd446d25f.jpg   Intermediate    \N      1222220 f      \N       test    \N      test    14      4       1       \N      PUBLISHED
34      2024-08-10 09:33:06.165926      2024-08-10 09:33:33.625254              t               http://res.cloudinary.com/di6h4mtfa/image/upload/v1723257213/10fe2795-7f49-4c0a-82d2-8a3d34d73d91.jpg   AllLevel        \N      \N      t      \N       Spring-Boot-3,-Spring-6-&-Hibernate-for-Beginners       \N      [NEW] Spring Boot 3, Spring 6 & Hibernate for Beginners 14      1       19      \N      PUBLISHED
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 \.


--
-- Data for Name: course_promotion; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.course_promotion (course_id, promotion_id) FROM stdin;
\.


--
-- Data for Name: exercise; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.exercise (id, created_at, updated_at, description, submission_deadline, title, classroom_id) FROM stdin;
\.


--
-- Data for Name: exercise_file; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.exercise_file (id, file_name, file_url, exercise_id) FROM stdin;
\.


--
-- Data for Name: learning_course; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.learning_course (id, course_id, student_id) FROM stdin;
1       6       3
2       1       3
4       6       1
5       2       1
6       1       1
7       5       1
8       2       13
9       20      12
10      18      12
11      1       12
12      19      12
13      21      12
14      33      1
15      34      1
16      34      16
17      8       16
18      27      16
19      3       16
20      5       16
21      3       3
\.


--
-- Data for Name: learning_lecture; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.learning_lecture (id, access_time, finished, watching_second, lecture_id, student_id) FROM stdin;
1       2024-08-03 07:49:54.532504      t       24      1       3
2       2024-08-03 07:50:03.511936      t       75      4       3
3       2024-08-03 07:50:07.65211       t       75      2       3
4       2024-07-30 22:27:14.18473       f       13      3       3
5       2024-08-03 07:50:11.50495       f       0       5       3
11      2024-08-10 11:03:36.515669      t       0       15      16
10      2024-08-15 12:55:57.549313      f       10      2       1
12      2024-08-15 13:12:01.178858      f       10      12      1
7       2024-08-15 13:12:02.054773      f       17      4       1
8       2024-08-15 13:12:03.639959      f       17      1       1
14      2024-08-15 13:13:59.614707      f       0       14      1
9       2024-08-15 13:23:28.60151       t       19      5       1
13      2024-08-15 13:23:29.201042      f       10      3       1
15      2025-05-16 17:05:42.280836      t       0       13      1
16      2025-06-12 21:20:55.851062      t       0       1       1
17      2025-06-12 21:20:56.86939       t       0       4       1
18      2025-06-12 21:20:59.34991       t       0       12      1
19      2025-06-12 21:21:01.903775      t       0       2       1
20      2025-06-12 21:21:19.466253      f       0       3       1
21      2025-06-12 21:27:13.420714      f       0       3       3
22      2025-06-12 21:28:11.362036      t       0       12      3
23      2025-06-12 21:28:14.817717      t       0       3       3
24      2025-06-12 21:28:20.871843      t       0       5       3
\.


--
-- Data for Name: learning_quiz; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.learning_quiz (id, access_time, finished, quiz_id, student_id) FROM stdin;
1       2024-08-03 07:50:05.803549      t       1       3
4       2024-08-10 10:07:23.961336      f       6       1
3       2025-06-12 21:20:58.364362      t       1       1
\.


--
-- Data for Name: lecture; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.lecture (id, created_at, updated_at, duration, lecture_details, number, title, video_id, section_id) FROM stdin;
1       2024-07-16 21:31:08.330415      2024-07-16 21:31:08.330415      3600    \N      -1      first Lecture   http://res.cloudinary.com/di6h4mtfa/video/upload/v1721228036/202d9a90-94de-416c-89fc-1996a1360b8a.mp4   1
2       2024-07-16 21:31:08.330415      2024-07-16 21:31:08.330415      60      \N      -1      What are you doing?    http://res.cloudinary.com/di6h4mtfa/video/upload/v1722223814/edfbb048-c250-4326-b001-915a84563b91.mp4    2
3       2024-07-16 21:31:08.330415      2024-07-16 21:31:08.330415      59      \N      0       test33  http://res.cloudinary.com/di6h4mtfa/video/upload/v1721228036/202d9a90-94de-416c-89fc-1996a1360b8a.mp4   2
4       2024-07-16 21:31:08.330415      2024-07-16 21:31:08.330415      59      \N      0       heheee  http://res.cloudinary.com/di6h4mtfa/video/upload/v1721228036/202d9a90-94de-416c-89fc-1996a1360b8a.mp4   1
5       2024-07-16 21:31:08.330415      2024-07-16 21:31:08.330415      59      \N      -1      Sum up  http://res.cloudinary.com/di6h4mtfa/video/upload/v1721228036/202d9a90-94de-416c-89fc-1996a1360b8a.mp4   3
7       2024-07-16 21:31:08.330415      2024-07-16 21:31:08.330415      59      \N      -1      first lecture   http://res.cloudinary.com/di6h4mtfa/video/upload/v1723096005/e18a65cd-c1f1-4c59-b592-a7ea8a382e75.mp4   5
8       2024-07-16 21:31:08.330415      2024-07-16 21:31:08.330415      22      \N      1       second lecture  http://res.cloudinary.com/di6h4mtfa/video/upload/v1723096117/ac1b3325-eef2-468f-bb90-2c4f0348381c.mp4   5
9       2024-07-16 21:31:08.330415      2024-07-16 21:31:08.330415      22      \N      2       third lecture   http://res.cloudinary.com/di6h4mtfa/video/upload/v1723096193/ef3fee2e-30a1-4f3b-91e2-5614c289b27d.mp4   5
10      2024-07-16 21:31:08.330415      2024-07-16 21:31:08.330415      22      \N      -1      tsaaa   http://res.cloudinary.com/di6h4mtfa/video/upload/v1723097014/3a559a2b-13f8-47ce-a192-27d205bcf7da.mp4   6
11      2024-07-16 21:31:08.330415      2024-07-16 21:31:08.330415      22      \N      0       asaa    http://res.cloudinary.com/di6h4mtfa/video/upload/v1723097165/6ed66bbe-bd12-4c02-a261-6ca0c1f34b81.mp4   6
12      2024-07-16 21:31:08.330415      2024-07-16 21:31:08.330415      22      \N      2       lasdfa  http://res.cloudinary.com/di6h4mtfa/video/upload/v1723106713/d047423e-e3fd-4613-a279-d89c08d72a94.mp4   1
13      2024-07-16 21:31:08.330415      2024-07-16 21:31:08.330415      22      \N      -1      test    http://res.cloudinary.com/di6h4mtfa/video/upload/v1723188173/6e9cf58a-594a-4532-9b0a-44069ac96a46.mp4   7
14      2024-07-16 21:31:08.330415      2024-07-16 21:31:08.330415      22      \N      -1      aaa     http://res.cloudinary.com/di6h4mtfa/video/upload/v1723210885/81964a30-7b34-4b62-81ff-90130730713c.mp4   9
15      2024-07-16 21:31:08.330415      2024-07-16 21:31:08.330415      22      \N      -1      fafaf   http://res.cloudinary.com/di6h4mtfa/video/upload/v1723257342/6899f789-6d01-4e0b-8ad1-806c5948fbbf.mp4   10
\.


--
-- Data for Name: meeting; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.meeting (id, created_at, updated_at, code, end_time, start_time, classroom_id) FROM stdin;
\.


--
-- Data for Name: note; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.note (id, content, "time", lecture_id, student_id) FROM stdin;
3       <p>note at 15s</p>      15      12      1
6       <p>note at chuong 3</p> 36      5       1
7       <p>afafa</p>    10      1       1
\.


--
-- Data for Name: order_detail; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.order_detail (id, price, course_id, order_id) FROM stdin;
1       200000  1       1
2       200000  2       1
3       200000  3       1
4       200000  4       2
5       200000  5       2
6       200000  6       3
7       200000  1       6
8       200000  2       7
9       200000  3       8
10      200000  2       9
11      200000  3       9
12      200000  4       9
13      200000  5       9
14      200000  2       10
16      200000  6       12
17      200000  2       13
18      200000  1       14
19      200000  5       15
20      200000  5       16
21      200000  5       17
22      200000  5       18
23      200000  5       19
24      200000  2       20
25      200000  2       21
26      200000  2       22
27      200000  20      23
28      199999  18      24
29      199000  1       25
30      100000  19      26
31      200000  21      27
32      1222220 33      28
33      200000  8       29
35      200000  27      30
36      200000  3       32
37      200000  5       33
38      200000  3       34
\.


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.orders (id, created_at, status, coupon_id, student_id, reason_failed) FROM stdin;
1       2024-07-16 21:31:08.330415      PENDING \N      3       \N
2       2024-07-16 22:35:39.421462      PENDING \N      3       \N
3       2024-07-16 22:41:47.900013      PENDING \N      3       \N
4       2024-07-16 22:44:23.30656       PENDING \N      3       \N
5       2024-07-16 22:44:37.743554      PENDING \N      3       \N
6       2024-07-16 22:45:13.58787       PENDING \N      3       \N
7       2024-07-16 22:55:42.64783       PENDING \N      3       \N
8       2024-07-17 10:23:24.813899      SUCCESS \N      3       \N
9       2024-07-17 10:46:05.780298      SUCCESS \N      3       \N
10      2024-08-03 07:50:38.221376      SUCCESS \N      3       \N
12      2024-08-08 02:22:09.267496      SUCCESS \N      1       \N
13      2024-08-08 02:34:11.24871       SUCCESS \N      1       \N
14      2024-08-08 03:16:30.794969      SUCCESS \N      1       \N
15      2024-08-08 16:07:27.127164      PENDING \N      1       \N
16      2024-08-08 16:08:01.090109      PENDING \N      1       \N
17      2024-08-08 16:08:21.891892      PENDING \N      1       \N
18      2024-08-08 16:09:55.603138      SUCCESS \N      1       \N
19      2024-08-09 13:40:12.39998       PENDING \N      13      \N
20      2024-08-09 13:47:21.071082      PENDING \N      13      \N
21      2024-08-09 13:52:15.911406      PENDING \N      13      \N
22      2024-08-09 13:53:56.224387      SUCCESS \N      13      \N
23      2024-08-09 18:20:30.888692      SUCCESS \N      12      \N
24      2024-08-09 18:23:07.083358      SUCCESS \N      12      \N
25      2024-08-09 18:26:21.282728      SUCCESS \N      12      \N
26      2024-08-09 18:27:58.144713      SUCCESS \N      12      \N
27      2024-08-09 18:28:37.966969      SUCCESS \N      12      \N
28      2024-08-09 20:49:59.528505      SUCCESS \N      1       \N
29      2024-08-10 11:10:03.303485      SUCCESS \N      16      \N
30      2024-08-10 11:21:53.652767      SUCCESS 7       16      \N
32      2024-08-10 11:26:15.942093      SUCCESS 7       16      \N
33      2024-08-10 11:27:19.164846      SUCCESS 7       16      \N
34      2025-06-12 21:30:08.753686      SUCCESS \N      3       \N
\.


--
-- Data for Name: payment; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.payment (id, amount, bank_code, bank_tran_no, cart_type, pay_date, order_id, card_type) FROM stdin;
1       60000000        NCB     VNP14514775     ATM     2024-01-16 21:35:02     1       \N
2       20000000        NCB     VNP14548595     ATM     2024-08-08 02:22:57     12      \N
3       40000000        NCB     VNP14514918     ATM     2024-02-16 21:35:02     2       \N
4       20000000        NCB     VNP14514929     ATM     2024-03-16 21:35:02     3       \N
5       20000000        NCB     VNP14514940     ATM     2024-04-16 21:35:02     6       \N
6       20000000        NCB     VNP14514964     ATM     2024-05-16 21:35:02     7       \N
7       20000000        NCB     VNP14515642     ATM     2024-07-16 21:35:02     8       \N
8       80000000        NCB     VNP14515703     ATM     2024-07-15 21:35:02     9       \N
9       20000000        NCB     VNP14543281     ATM     2024-08-03 07:51:21     10      \N
10      20000000        NCB     VNP14514775     ATM     2024-08-08 21:35:02     13      \N
12      20000000        NCB     VNP14548603     ATM     2024-08-08 03:16:52     14      \N
13      20000000        NCB     VNP14549367     ATM     2024-08-08 16:10:22     18      \N
15      20000000        NCB     VNP14550307     ATM     2024-08-09 13:40:51     19      \N
16      20000000        NCB     VNP14550313     ATM     2024-08-09 13:47:43     20      \N
19      20000000        NCB     VNP14550322     ATM     2024-08-09 13:52:37     21      \N
21      20000000        NCB     VNP14550324     ATM     2024-08-09 13:54:16     22      \N
24      20000000        NCB     VNP14550787     ATM     2024-08-09 18:21:10     23      \N
25      19999900        NCB     VNP14550790     ATM     2024-08-09 18:23:29     24      \N
27      19900000        NCB     VNP14550795     ATM     2024-08-09 18:26:41     25      \N
29      10000000        NCB     VNP14550796     ATM     2024-08-09 18:28:14     26      \N
31      20000000        NCB     VNP14550797     ATM     2024-08-09 18:28:55     27      \N
33      122222000       NCB     VNP14550854     ATM     2024-08-09 20:50:39     28      \N
35      17600000        NCB     VNP14551060     ATM     2024-08-10 11:10:43     29      \N
37      17000000        NCB     VNP14551071     ATM     2024-08-10 11:22:10     30      \N
39      17000000        NCB     VNP14551074     ATM     2024-08-10 11:26:35     32      \N
40      17000000        NCB     VNP14551076     ATM     2024-08-10 11:27:37     33      \N
41      20000000        NCB     VNP15015216     \N      2025-06-12 21:31:12     34      \N
\.


--
-- Data for Name: promotion; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.promotion (id, discount_percent, end_time, name, start_time) FROM stdin;
\.


--
-- Data for Name: question; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.question (id, created_at, updated_at, title, quiz_id) FROM stdin;
1       \N      \N      <p>are you ok</p>       1
2       \N      \N      <p>are you handsome?</p>        1
5       \N      \N      <p>test ?</p>   4
6       \N      \N      <p>test ?</p>   4
7       \N      \N      <p>test ?</p>   4
8       \N      \N      <p>aaa</p>      5
9       \N      \N      <p>aaaa</p>     6
10      \N      \N      <p>aaaaaa</p>   6
\.


--
-- Data for Name: question_lecture; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.question_lecture (id, created_at, updated_at, description, title, lecture_id, student_id) FROM stdin;
1       2025-06-12 21:21:58.010271      2025-06-12 21:21:58.010271      tại sao lại làm ntn?    Tại sao 3       1
\.


--
-- Data for Name: quiz; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.quiz (id, created_at, updated_at, description, number, title, section_id) FROM stdin;
1       2024-07-16 21:31:08.330415      2024-07-16 21:31:08.330415              1       quizzzzzz       1
4       \N      \N      <p>desc</p>     0       first quiz      5
5       \N      \N              -1      1aa     8
6       \N      \N              0       afaa    9
\.


--
-- Data for Name: reference; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.reference (id, created_at, updated_at, description, classroom_id) FROM stdin;
\.


--
-- Data for Name: reference_file; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.reference_file (id, file_name, file_url, reference_id) FROM stdin;
\.


--
-- Data for Name: review; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.review (id, content, created_at, rating_star, status, updated_at, course_id, student_id, reason_refused) FROM stdin;
22      aaaaa   2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      9       11     \N
23      aaaa    2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      10      11     \N
24      aaaa    2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      11      11     \N
26      fffffffff       2024-07-25 19:37:56.165958      4       PUBLISHED       2024-07-25 19:37:56.165958      13     11       \N
27      fffffffff       2024-07-25 19:37:56.165958      4       PUBLISHED       2024-07-25 19:37:56.165958      14     10       \N
28      aaaa    2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      15      9      \N
29      aaaa    2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      16      8      \N
30      aaaaa   2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      17      7      \N
31      aaa     2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      18      7      \N
32      aaaa    2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      19      8      \N
33      good\n  2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      20      9      \N
34      hay\n   2024-07-25 19:37:56.165958      4       PUBLISHED       2024-07-25 19:37:56.165958      21      11     \N
35      very good course        2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958     22       10      \N
36      amazing course, i can say       2024-07-25 19:38:21.603742      4       PUBLISHED       2024-08-02 22:32:57.989635      23      6       \N
37      amazing course, i can say       2024-07-25 19:38:21.603742      4       PUBLISHED       2024-08-02 22:32:57.989635      32      6       \N
38      very good course        2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958     31       10      \N
39      hay\n   2024-07-25 19:37:56.165958      4       PUBLISHED       2024-07-25 19:37:56.165958      30      11     \N
40      good\n  2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      29      9      \N
41      aaaa    2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      28      8      \N
42      aaa     2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      27      7      \N
43      aaaaa   2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      26      7      \N
44      aaaa    2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      25      8      \N
45      aaaa    2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      24      9      \N
46      amazing course, i can say       2024-07-25 19:38:21.603742      4       PUBLISHED       2024-08-02 22:32:57.989635      12      6       \N
47      its so perfect  2024-08-10 11:03:30.064882      5       PUBLISHED       2024-08-10 11:03:30.064882      34     16       \N
48      gg      2024-08-10 11:28:07.880948      5       PUBLISHED       2024-08-10 11:28:07.880948      8       16     \N
49      aaa     2024-08-10 11:28:11.239474      5       PUBLISHED       2024-08-10 11:28:11.239474      27      16     \N
50      quite good\n    2024-08-10 11:28:16.790747      5       PUBLISHED       2024-08-10 11:28:16.790747      3      16       \N
1       very good course        2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958     11       \N
2       amazing course, i can say       2024-07-25 19:38:21.603742      4       PUBLISHED       2024-08-02 22:32:57.989635      1       3       \N
3       nice    2024-07-25 19:38:27.237839      4       PUBLISHED       2024-07-25 19:37:56.165958      1       4      \N
4       good    2024-07-25 19:38:30.407122      4       PUBLISHED       2024-07-25 19:37:56.165958      1       5      \N
5       a little nice   2024-07-25 19:38:34.040981      4       PUBLISHED       2024-07-25 19:37:56.165958      1      6\N
6       quite bad       2024-07-25 19:38:37.517754      3       PUBLISHED       2024-07-25 19:37:56.165958      1      7\N
7       middle course   2024-07-25 19:37:56.165958      3       PUBLISHED       2024-07-25 19:37:56.165958      1      8\N
8       medium course   2024-07-25 19:38:21.603742      3       PUBLISHED       2024-07-25 19:37:56.165958      1      9\N
9       fair    2024-07-25 19:38:27.237839      3       PUBLISHED       2024-07-25 19:37:56.165958      1       10     \N
10      fair    2024-07-25 19:38:30.407122      2       PUBLISHED       2024-07-25 19:37:56.165958      1       11     \N
11      bad     2024-07-25 19:38:34.040981      4       PUBLISHED       2024-07-25 19:37:56.165958      1       12     \N
12      very bad        2024-07-25 19:38:37.517754      1       PUBLISHED       2024-07-25 19:37:56.165958      12     13       \N
13      test    2024-07-25 19:38:37.517754      5       PUBLISHED       2024-07-25 19:38:37.517754      6       4      \N
14      highly recommend        2024-08-08 04:08:59.109011      4       PUBLISHED       2024-08-08 04:08:59.109011     61       \N
15      quite good      2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      2      1\N
16      nice    2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      3       13     \N
17      hay\n   2024-07-25 19:37:56.165958      4       PUBLISHED       2024-07-25 19:37:56.165958      4       13     \N
18      good\n  2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      5       13     \N
19      aaaa    2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      6       12     \N
21      aaa     2024-07-25 19:37:56.165958      5       PUBLISHED       2024-07-25 19:37:56.165958      8       12     \N
51      nice    2024-08-10 11:28:20.445274      5       PUBLISHED       2024-08-10 11:28:20.445274      5       16     \N
\.


--
-- Data for Name: review_classroom; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.review_classroom (id, content, created_at, rating_star, status, updated_at, classroom_id, student_id) FROM stdin;
\.


--
-- Data for Name: section; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.section (id, created_at, updated_at, number, objective, title, course_id) FROM stdin;
1       \N      \N      0       \N      first section   1
2       \N      \N      1       \N      Middle Section  1
3       \N      \N      2       \N      Final section   1
5       \N      \N      0       \N      first section   8
6       \N      \N      1       asdfsf asdfa    Final section   8
7       \N      \N      0       \N      first section   2
8       \N      \N      0       \N      frsa    3
9       \N      \N      0       \N      first sec       33
10      \N      \N      0       afa     first section   34
\.


--
-- Data for Name: student; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.student (id, created_at, updated_at, active, date_of_birth, email, first_name, gender, last_name, password, photo, verification_code, verification_expiration) FROM stdin;
4       2024-07-12 08:11:57.116092      2024-07-12 08:11:57.116092      t       \N      thuanngo3072003@gmail.com      A\N      thuan   $2a$10$QmtGER8iZhPfdum80AOBXe/wSMkNvmf9nLyXKrYhVF7wHbh4ZD8Si    https://lh3.googleusercontent.com/a/ACg8ocKVglZXhFpPUA45hoaFsrHxiypaQy_TafqtQWuHockGL0-ciA=s96-c        \N      \N
5       2024-07-12 08:11:57.116092      2024-07-12 08:11:57.116092      t       \N      thuanngo3072004@gmail.com      B\N      thuan   $2a$10$QmtGER8iZhPfdum80AOBXe/wSMkNvmf9nLyXKrYhVF7wHbh4ZD8Si    https://lh3.googleusercontent.com/a/ACg8ocKVglZXhFpPUA45hoaFsrHxiypaQy_TafqtQWuHockGL0-ciA=s96-c        \N      \N
7       2024-07-12 10:54:31.707485      2024-07-12 10:54:31.707485      t       \N      thuanngo3072006@gmail.com      E\N      thuan   $2a$10$QmtGER8iZhPfdum80AOBXe/wSMkNvmf9nLyXKrYhVF7wHbh4ZD8Si    https://lh3.googleusercontent.com/a/ACg8ocJed28hNobmEPYaoDEgrJQBSqEpjp0lYdrqIOyjqesAkSohHA=s96-c        \N      \N
8       2024-07-12 08:11:57.116092      2024-07-12 08:11:57.116092      t       \N      thuanngo3072007@gmail.com      D\N      thuan   $2a$10$QmtGER8iZhPfdum80AOBXe/wSMkNvmf9nLyXKrYhVF7wHbh4ZD8Si    https://lh3.googleusercontent.com/a/ACg8ocKVglZXhFpPUA45hoaFsrHxiypaQy_TafqtQWuHockGL0-ciA=s96-c        \N      \N
11      2024-07-12 10:54:31.707485      2024-07-12 10:54:31.707485      t       \N      thuanngo3072010@gmail.com      H\N      thuan   $2a$10$QmtGER8iZhPfdum80AOBXe/wSMkNvmf9nLyXKrYhVF7wHbh4ZD8Si    https://lh3.googleusercontent.com/a/ACg8ocJed28hNobmEPYaoDEgrJQBSqEpjp0lYdrqIOyjqesAkSohHA=s96-c        \N      \N
12      2024-07-12 08:11:57.116092      2024-07-12 08:11:57.116092      t       \N      thuanngo3072001@gmail.com      E\N      D20CQCN02-N     $2a$10$QmtGER8iZhPfdum80AOBXe/wSMkNvmf9nLyXKrYhVF7wHbh4ZD8Si    https://lh3.googleusercontent.com/a/ACg8ocKVglZXhFpPUA45hoaFsrHxiypaQy_TafqtQWuHockGL0-ciA=s96-c        \N      \N
3       2024-07-12 10:54:31.707485      2024-07-12 10:54:31.707485      t       \N      thuanngo3072002@gmail.com      ngo      \N      thuann  $2a$10$QmtGER8iZhPfdum80AOBXe/wSMkNvmf9nLyXKrYhVF7wHbh4ZD8Si    https://lh3.googleusercontent.com/a/ACg8ocJed28hNobmEPYaoDEgrJQBSqEpjp0lYdrqIOyjqesAkSohHA=s96-c        \N      \N
1       2024-07-12 08:11:57.116092      2024-07-12 08:11:57.116092      t       \N      n20dccn153@student.ptithcm.edu.vn       NGO DUC THUAN   \N      D20CQCN02-N     $2a$10$Twr0ozT3FRUAq4tIB5HLrOHQOYddctTrvAPV5yPw.9uX1vlglUGEK    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723064802/1248d14b-cc53-425a-8640-29bc9826fccd.jpg   \N      \N
15      2024-07-12 10:54:31.707485      2024-07-12 10:54:31.707485      t       \N      thaongo3072002@gmail.com       ngo      \N      thao    $2a$10$QmtGER8iZhPfdum80AOBXe/wSMkNvmf9nLyXKrYhVF7wHbh4ZD8Si    https://lh3.googleusercontent.com/a/ACg8ocJed28hNobmEPYaoDEgrJQBSqEpjp0lYdrqIOyjqesAkSohHA=s96-c        \N      \N
6       2024-07-12 08:11:57.116092      2024-07-12 08:11:57.116092      t       \N      thuanngo3072005@gmail.com      k\N      D20CQCN02-N     $2a$10$QmtGER8iZhPfdum80AOBXe/wSMkNvmf9nLyXKrYhVF7wHbh4ZD8Si    https://lh3.googleusercontent.com/a/ACg8ocKVglZXhFpPUA45hoaFsrHxiypaQy_TafqtQWuHockGL0-ciA=s96-c        \N      \N
13      2024-07-12 10:54:31.707485      2024-07-12 10:54:31.707485      t       \N      thuanngo3072000@gmail.com      F\N      thuan   $2a$10$QmtGER8iZhPfdum80AOBXe/wSMkNvmf9nLyXKrYhVF7wHbh4ZD8Si    https://lh3.googleusercontent.com/a/ACg8ocJed28hNobmEPYaoDEgrJQBSqEpjp0lYdrqIOyjqesAkSohHA=s96-c        \N      \N
9       2024-07-12 08:11:57.116092      2024-07-12 08:11:57.116092      t       \N      thuanngo3072008@gmail.com      F\N      thuan   $2a$10$QmtGER8iZhPfdum80AOBXe/wSMkNvmf9nLyXKrYhVF7wHbh4ZD8Si    https://lh3.googleusercontent.com/a/ACg8ocKVglZXhFpPUA45hoaFsrHxiypaQy_TafqtQWuHockGL0-ciA=s96-c        \N      \N
10      2024-07-12 08:11:57.116092      2024-07-12 08:11:57.116092      f       \N      thuanngo3072009@gmail.com      G\N      D20CQCN02-N     $2a$10$QmtGER8iZhPfdum80AOBXe/wSMkNvmf9nLyXKrYhVF7wHbh4ZD8Si    https://lh3.googleusercontent.com/a/ACg8ocKVglZXhFpPUA45hoaFsrHxiypaQy_TafqtQWuHockGL0-ciA=s96-c        \N      \N
16      2024-07-12 10:54:31.707485      2024-07-12 10:54:31.707485      t       2001-02-05      thuanngo00000@gmail.comNgo      \N      Thuan   $2a$10$QmtGER8iZhPfdum80AOBXe/wSMkNvmf9nLyXKrYhVF7wHbh4ZD8Si    https://lh3.googleusercontent.com/a/ACg8ocJed28hNobmEPYaoDEgrJQBSqEpjp0lYdrqIOyjqesAkSohHA=s96-c        \N      \N
\.


--
-- Data for Name: student_answer; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.student_answer (id, created_at, updated_at, content, question_lecture_id, student_id) FROM stdin;
1       2025-06-12 21:27:20.151542      2025-06-12 21:27:20.151542      À tôi hiểu r    1       1
2       2025-06-12 21:27:34.017211      2025-06-12 21:27:34.017211      là sao vậy bạn  1       3
3       2025-06-12 21:27:48.879 2025-06-12 21:27:48.879 bạn ko hiểu thật ạ      1       1
4       2025-06-12 21:27:55.135195      2025-06-12 21:27:55.135195      ừ       1       3
\.


--
-- Data for Name: student_exercise; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.student_exercise (id, file_name, file_url, submitted, submitted_time, exercise_id, student_id) FROM stdin;
\.


--
-- Data for Name: topic; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.topic (id, created_at, updated_at, description, name, publish) FROM stdin;
2       2024-06-30 18:02:14.648958      2024-06-30 18:02:14.648958      string  Javascript      t
3       2024-06-30 18:02:20.062415      2024-06-30 18:02:20.062415      string  React JS        t
4       2024-06-30 18:02:38.998262      2024-06-30 18:02:38.998262      string  HTML    t
5       2024-06-30 18:02:41.373338      2024-06-30 18:02:41.373338      string  CSS     t
20      2024-08-07 20:04:35.673465      2024-08-07 20:05:13.237552      desc    Next js t
1       2024-06-30 18:01:55.210496      2024-08-08 06:23:55.122702      string  Java    t
19      2024-08-07 20:01:17.529059      2024-08-08 06:27:12.927241      desc    Angular f
21      2024-08-09 20:10:34.95478       2024-08-09 20:10:34.95478       afaf    Javascript      t
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public."user" (id, created_at, updated_at, active, date_of_birth, email, first_name, gender, headline, last_name, password, photo, role) FROM stdin;
21      2024-08-09 19:51:03.166684      2024-08-09 19:51:23.789426      t       2002-03-02      aa2222@example.com     ngo      MALE    \N      thuan   $2a$10$ewv6dgJr15wFUaSNFideYezogTnAkxVx0/Es/yPwzBpR9pNbbVfki    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723207883/92d06b49-da7f-4985-9694-55062ce9aecc.jpg   ROLE_INSTRUCTOR
6       2024-08-08 04:20:28.149578      2024-08-08 04:20:28.149578      t       1990-06-15      daniel.martinez@example.com     Daniel  MALE    \N      Martinez        $2a$10$9yKuD/cEnpo1uvMttwG.qewBbZCmC8Oe7Ege4wBlhMhqtdlB/UJCq    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723066377/df98811a-b057-4998-88ab-04577c0c9190.jpg   ROLE_INSTRUCTOR
7       2024-08-08 04:20:28.149578      2024-08-08 04:20:28.149578      t       1990-06-15      olivia.brown@example.comOlivia  MALE    \N      Brown   $2a$10$/ywmsN6E/vTUYQeh3rveXOLnev1GXhNlEf9gWfuecoj4CrSX0qrVq    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723066377/df98811a-b057-4998-88ab-04577c0c9190.jpg   ROLE_INSTRUCTOR
22      2024-08-09 20:00:33.193886      2024-08-09 20:01:00.651708      t       2002-02-04      aaaan@example.com      ngoaaaaa MALE    \N      thuan   $2a$10$I7YxwDIqZ2caZjJchhjMVejMmCGN0.ziPd5mhtkaNLAX9N1Xb40pq    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723208460/cc9ac8f5-1853-46fc-9ddc-7d68d3d0f0d3.jpg   ROLE_INSTRUCTOR
23      2024-08-09 20:05:34.049209      2024-08-09 20:05:56.140341      t       2002-04-04      aaaaoe@example.com     ngoaaaa  FEMALE  \N      thuan   $2a$10$hlftyRVFV97bBkJUwxq63eGRH3QEMMS8Gec.UiR05zfTy0DhI3YPC    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723208755/4475ea5e-6aed-46b8-a63c-8528c95141bb.jpg   ROLE_INSTRUCTOR
2       2024-08-08 04:20:28.149578      2024-08-10 08:31:15.387817      t       1991-06-15      john.doe@example.com   John     MALE    \N      Doe     $2a$10$CYacHZvYTdeLTD/B7DmDnelzQjgoJ7ygjRsRjGwbHwOlqHNPOHiuu    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723065627/4971abd0-82ee-4df4-a981-01b8fbd130c4.jpg   ROLE_INSTRUCTOR
3       2024-08-08 04:20:28.149578      2024-08-08 04:31:28.288752      t       1990-06-15      jane.smith@example.com Jane     MALE    \N      Smith   $2a$10$gpEfT6QfTdJ5wwx22dsqrOX74Bno6b.bvdvbkB7nIq3JJziF0c3/2    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723066287/dda776f2-662a-4b44-b3ce-711b171b31fe.jpg   ROLE_INSTRUCTOR
4       2024-08-08 04:20:28.149578      2024-08-08 04:32:57.841053      t       1990-06-15      michael.johnson@example.com     Michael MALE    \N      Johnson $2a$10$ZRXbiLexbksLqLPmegWcSOLWQvp2OiEGhubF3RquQmKKaQeJmuVxC    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723066377/df98811a-b057-4998-88ab-04577c0c9190.jpg   ROLE_INSTRUCTOR
8       2024-08-08 04:20:28.149578      2024-08-08 04:20:28.149578      t       1990-06-15      william.wilson@example.com      William MALE    \N      Wilson  $2a$10$WqJkO3fVDyc/24Lu2UqJfe6YTCARRHLoZ9WBoaQoYo2WGQW7nI4qG    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723066377/df98811a-b057-4998-88ab-04577c0c9190.jpg   ROLE_INSTRUCTOR
1       2024-07-12 07:55:11.478226      2024-08-10 08:35:37.006482      t       2002-07-30      thuanngo3072006@gmail.com       Ngo     MALE    Software Engineer, Spring Certified     Thuan   $2a$10$WrUrR6GpnKJ3epkxZRFIiOGE5oXtQYSF3iBtrW8C4rJgo./g2v4ky    https://lh3.googleusercontent.com/a/ACg8ocKVglZXhFpPUA45hoaFsrHxiypaQy_TafqtQWuHockGL0-ciA=s96-c       ROLE_ADMIN
5       2024-08-08 04:20:28.149578      2024-08-08 04:20:28.149578      t       1990-06-15      emily.davis@example.comEmily    MALE    \N      Davis   $2a$10$IUu4fhjiFJ5ZEeJFs90rq.Wd1AP97wHM2LvbRyaYwoDRoa.O5dco6    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723066377/df98811a-b057-4998-88ab-04577c0c9190.jpg   ROLE_INSTRUCTOR
9       2024-08-08 04:20:28.149578      2024-08-08 04:20:28.149578      t       1990-06-15      ava.taylor@example.com Ava      MALE    \N      Taylor  $2a$10$.iE47veYv5MoiQYiluiHxu0JEwggaioym37WXf4/uQMqLD3cZMst.    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723066377/df98811a-b057-4998-88ab-04577c0c9190.jpg   ROLE_INSTRUCTOR
10      2024-08-08 04:20:28.149578      2024-08-08 04:20:28.149578      t       1990-06-15      james.anderson@example.com      James   MALE    \N      Anderson        $2a$10$9IUGLAqog/Cl7mM57oIOyeWqptVtVk0KES5oPqmbBtYYaNNt5M5zG    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723066377/df98811a-b057-4998-88ab-04577c0c9190.jpg   ROLE_INSTRUCTOR
11      2024-08-08 04:20:28.149578      2024-08-08 04:20:28.149578      t       1990-06-15      sophia.thomas@example.com       Sophia  MALE    \N      Thomas  $2a$10$P7/05SumJiNangXhmgCykeIUNQwSfqefcfyY3DD4lnqXtQiiSQx52    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723066377/df98811a-b057-4998-88ab-04577c0c9190.jpg   ROLE_INSTRUCTOR
12      2024-08-08 04:20:28.149578      2024-08-08 04:20:28.149578      t       1999-06-15      benjamin.jackson@example.com    Benjamin        MALE    \N      Jackson $2a$10$WrUrR6GpnKJ3epkxZRFIiOGE5oXtQYSF3iBtrW8C4rJgo./g2v4ky    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723066377/df98811a-b057-4998-88ab-04577c0c9190.jpg   ROLE_INSTRUCTOR
19      2024-08-09 19:47:19.442114      2024-08-09 19:47:43.291996      t       2002-07-03      thuanngo3072k12@yahoo.com       ngo     MALE    \N      thuann  $2a$10$pmcSJLlPUx5DI4jjWZGOa.g9G5UAOaXNChcCBp6QkNFOUMpG..FQG    http://res.cloudinary.com/di6h4mtfa/image/upload/v1723207662/2625f564-d4f7-4258-b0a4-532ad88029ad.jpg   ROLE_INSTRUCTOR
\.


--
-- Data for Name: user_answer; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.user_answer (id, created_at, updated_at, content, question_lecture_id, user_id) FROM stdin;
\.


--
-- Name: answer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.answer_id_seq', 13, true);


--
-- Name: cart_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.cart_id_seq', 9, true);


--
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.category_id_seq', 23, true);


--
-- Name: classroom_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.classroom_id_seq', 1, false);


--
-- Name: coupon_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.coupon_id_seq', 8, true);


--
-- Name: course_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.course_id_seq', 34, true);


--
-- Name: exercise_file_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.exercise_file_id_seq', 1, false);


--
-- Name: exercise_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.exercise_id_seq', 1, false);


--
-- Name: learning_course_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.learning_course_id_seq', 21, true);


--
-- Name: learning_lecture_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.learning_lecture_id_seq', 24, true);


--
-- Name: learning_quiz_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.learning_quiz_id_seq', 4, true);


--
-- Name: lecture_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.lecture_id_seq', 15, true);


--
-- Name: meeting_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.meeting_id_seq', 1, false);


--
-- Name: note_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.note_id_seq', 7, true);


--
-- Name: order_detail_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.order_detail_id_seq', 38, true);


--
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.orders_id_seq', 34, true);


--
-- Name: payment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.payment_id_seq', 41, true);


--
-- Name: promotion_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.promotion_id_seq', 1, false);


--
-- Name: question_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.question_id_seq', 10, true);


--
-- Name: question_lecture_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.question_lecture_id_seq', 1, true);


--
-- Name: quiz_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.quiz_id_seq', 6, true);


--
-- Name: reference_file_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.reference_file_id_seq', 1, false);


--
-- Name: reference_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.reference_id_seq', 1, false);


--
-- Name: review_classroom_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.review_classroom_id_seq', 1, false);


--
-- Name: review_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.review_id_seq', 51, true);


--
-- Name: section_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.section_id_seq', 10, true);


--
-- Name: student_answer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.student_answer_id_seq', 4, true);


--
-- Name: student_exercise_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.student_exercise_id_seq', 1, false);


--
-- Name: student_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.student_id_seq', 16, true);


--
-- Name: topic_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.topic_id_seq', 21, true);


--
-- Name: user_answer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.user_answer_id_seq', 1, false);


--
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.user_id_seq', 23, true);


--
-- Name: answer answer_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.answer
    ADD CONSTRAINT answer_pkey PRIMARY KEY (id);


--
-- Name: cart cart_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT cart_pkey PRIMARY KEY (id);


--
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- Name: category_topic category_topic_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.category_topic
    ADD CONSTRAINT category_topic_pkey PRIMARY KEY (topic_id, category_id);


--
-- Name: classroom classroom_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.classroom
    ADD CONSTRAINT classroom_pkey PRIMARY KEY (id);


--
-- Name: coupon coupon_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.coupon
    ADD CONSTRAINT coupon_pkey PRIMARY KEY (id);


--
-- Name: course course_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (id);


--
-- Name: course_promotion course_promotion_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.course_promotion
    ADD CONSTRAINT course_promotion_pkey PRIMARY KEY (course_id, promotion_id);


--
-- Name: exercise_file exercise_file_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.exercise_file
    ADD CONSTRAINT exercise_file_pkey PRIMARY KEY (id);


--
-- Name: exercise exercise_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.exercise
    ADD CONSTRAINT exercise_pkey PRIMARY KEY (id);


--
-- Name: learning_course learning_course_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.learning_course
    ADD CONSTRAINT learning_course_pkey PRIMARY KEY (id);


--
-- Name: learning_lecture learning_lecture_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.learning_lecture
    ADD CONSTRAINT learning_lecture_pkey PRIMARY KEY (id);


--
-- Name: learning_quiz learning_quiz_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.learning_quiz
    ADD CONSTRAINT learning_quiz_pkey PRIMARY KEY (id);


--
-- Name: lecture lecture_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.lecture
    ADD CONSTRAINT lecture_pkey PRIMARY KEY (id);


--
-- Name: meeting meeting_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT meeting_pkey PRIMARY KEY (id);


--
-- Name: note note_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.note
    ADD CONSTRAINT note_pkey PRIMARY KEY (id);


--
-- Name: order_detail order_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.order_detail
    ADD CONSTRAINT order_detail_pkey PRIMARY KEY (id);


--
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- Name: payment payment_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT payment_pkey PRIMARY KEY (id);


--
-- Name: promotion promotion_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.promotion
    ADD CONSTRAINT promotion_pkey PRIMARY KEY (id);


--
-- Name: question_lecture question_lecture_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.question_lecture
    ADD CONSTRAINT question_lecture_pkey PRIMARY KEY (id);


--
-- Name: question question_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.question
    ADD CONSTRAINT question_pkey PRIMARY KEY (id);


--
-- Name: quiz quiz_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.quiz
    ADD CONSTRAINT quiz_pkey PRIMARY KEY (id);


--
-- Name: reference_file reference_file_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.reference_file
    ADD CONSTRAINT reference_file_pkey PRIMARY KEY (id);


--
-- Name: reference reference_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.reference
    ADD CONSTRAINT reference_pkey PRIMARY KEY (id);


--
-- Name: review_classroom review_classroom_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.review_classroom
    ADD CONSTRAINT review_classroom_pkey PRIMARY KEY (id);


--
-- Name: review review_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.review
    ADD CONSTRAINT review_pkey PRIMARY KEY (id);


--
-- Name: section section_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.section
    ADD CONSTRAINT section_pkey PRIMARY KEY (id);


--
-- Name: student_answer student_answer_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.student_answer
    ADD CONSTRAINT student_answer_pkey PRIMARY KEY (id);


--
-- Name: student_exercise student_exercise_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.student_exercise
    ADD CONSTRAINT student_exercise_pkey PRIMARY KEY (id);


--
-- Name: student student_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_pkey PRIMARY KEY (id);


--
-- Name: topic topic_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.topic
    ADD CONSTRAINT topic_pkey PRIMARY KEY (id);


--
-- Name: category uk_46ccwnsi9409t36lurvtyljak; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT uk_46ccwnsi9409t36lurvtyljak UNIQUE (name);


--
-- Name: coupon uk_bg4p9ontpj7adq7yr71h93sdn; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.coupon
    ADD CONSTRAINT uk_bg4p9ontpj7adq7yr71h93sdn UNIQUE (code);


--
-- Name: student uk_fe0i52si7ybu0wjedj6motiim; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT uk_fe0i52si7ybu0wjedj6motiim UNIQUE (email);


--
-- Name: payment uk_mf7n8wo2rwrxsd6f3t9ub2mep; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT uk_mf7n8wo2rwrxsd6f3t9ub2mep UNIQUE (order_id);


--
-- Name: course uk_msgoex7rold2eqqf1cllhk02i; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT uk_msgoex7rold2eqqf1cllhk02i UNIQUE (title);


--
-- Name: user uk_ob8kqyqqgmefl0aco34akdtpe; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT uk_ob8kqyqqgmefl0aco34akdtpe UNIQUE (email);


--
-- Name: user_answer user_answer_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.user_answer
    ADD CONSTRAINT user_answer_pkey PRIMARY KEY (id);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: category fk2y94svpmqttx80mshyny85wqr; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT fk2y94svpmqttx80mshyny85wqr FOREIGN KEY (parent_id) REFERENCES public.category(id);


--
-- Name: learning_lecture fk42x9a9ir0u0hs3k3hdox19p9p; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.learning_lecture
    ADD CONSTRAINT fk42x9a9ir0u0hs3k3hdox19p9p FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: classroom fk478bp6bk6381i6r1nqfp0kbgs; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.classroom
    ADD CONSTRAINT fk478bp6bk6381i6r1nqfp0kbgs FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: student_exercise fk4wbc4u6c21xphxdb93m38say4; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.student_exercise
    ADD CONSTRAINT fk4wbc4u6c21xphxdb93m38say4 FOREIGN KEY (exercise_id) REFERENCES public.exercise(id);


--
-- Name: question_lecture fk4x8fsiicrvytqv0wc0lpunxx1; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.question_lecture
    ADD CONSTRAINT fk4x8fsiicrvytqv0wc0lpunxx1 FOREIGN KEY (lecture_id) REFERENCES public.lecture(id);


--
-- Name: learning_quiz fk53rg1l7invcbu9pxeh8neqtkn; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.learning_quiz
    ADD CONSTRAINT fk53rg1l7invcbu9pxeh8neqtkn FOREIGN KEY (quiz_id) REFERENCES public.quiz(id);


--
-- Name: lecture fk568elaju5okd8k0hukt18mtk7; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.lecture
    ADD CONSTRAINT fk568elaju5okd8k0hukt18mtk7 FOREIGN KEY (section_id) REFERENCES public.section(id);


--
-- Name: category_topic fk7bbgepmfwmj5nlvvjmp91lqtv; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.category_topic
    ADD CONSTRAINT fk7bbgepmfwmj5nlvvjmp91lqtv FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- Name: exercise fk7nf6wmt2xhkvibg1eiv5gb20p; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.exercise
    ADD CONSTRAINT fk7nf6wmt2xhkvibg1eiv5gb20p FOREIGN KEY (classroom_id) REFERENCES public.classroom(id);


--
-- Name: cart fk80vo7qdpfmj7u3g69vkx8o7vy; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT fk80vo7qdpfmj7u3g69vkx8o7vy FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: answer fk8frr4bcabmmeyyu60qt7iiblo; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.answer
    ADD CONSTRAINT fk8frr4bcabmmeyyu60qt7iiblo FOREIGN KEY (question_id) REFERENCES public.question(id);


--
-- Name: review_classroom fk8vboqc43i6i0ru3ojeugy0oi4; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.review_classroom
    ADD CONSTRAINT fk8vboqc43i6i0ru3ojeugy0oi4 FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: orders fka1w39imx35e4w12gc9tnrqk1c; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fka1w39imx35e4w12gc9tnrqk1c FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: orders fka5ei0aklq6wrjl8vrr7ied3bx; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fka5ei0aklq6wrjl8vrr7ied3bx FOREIGN KEY (coupon_id) REFERENCES public.coupon(id);


--
-- Name: review fkaple62pi4jtfvyepa4150jbrf; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.review
    ADD CONSTRAINT fkaple62pi4jtfvyepa4150jbrf FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: question fkb0yh0c1qaxfwlcnwo9dms2txf; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.question
    ADD CONSTRAINT fkb0yh0c1qaxfwlcnwo9dms2txf FOREIGN KEY (quiz_id) REFERENCES public.quiz(id);


--
-- Name: order_detail fkd81apmag4lbo3gw9b3n8myokq; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.order_detail
    ADD CONSTRAINT fkd81apmag4lbo3gw9b3n8myokq FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: note fke1nsdee55sw0ptvuoy867cln8; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.note
    ADD CONSTRAINT fke1nsdee55sw0ptvuoy867cln8 FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: cart fke8qhvp3rieyui6fxssjs4r34r; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT fke8qhvp3rieyui6fxssjs4r34r FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: course fkg4vkpa3s2ck9iipvl3lanpue7; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT fkg4vkpa3s2ck9iipvl3lanpue7 FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: note fkg9hurjpmixpukyptss8otaqyk; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.note
    ADD CONSTRAINT fkg9hurjpmixpukyptss8otaqyk FOREIGN KEY (lecture_id) REFERENCES public.lecture(id);


--
-- Name: learning_course fkil6b6kv8nrl91srw06ktipu10; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.learning_course
    ADD CONSTRAINT fkil6b6kv8nrl91srw06ktipu10 FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: student_answer fkiu9djhgc89s9fw4jgonous3ki; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.student_answer
    ADD CONSTRAINT fkiu9djhgc89s9fw4jgonous3ki FOREIGN KEY (question_lecture_id) REFERENCES public.question_lecture(id);


--
-- Name: learning_lecture fkj5262yslfa4knjtn59ch322qo; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.learning_lecture
    ADD CONSTRAINT fkj5262yslfa4knjtn59ch322qo FOREIGN KEY (lecture_id) REFERENCES public.lecture(id);


--
-- Name: review_classroom fkj7y9vxtwttoyqgsk86vhmya05; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.review_classroom
    ADD CONSTRAINT fkj7y9vxtwttoyqgsk86vhmya05 FOREIGN KEY (classroom_id) REFERENCES public.classroom(id);


--
-- Name: exercise_file fkk33yf1kl67o905gn1y2e4tfad; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.exercise_file
    ADD CONSTRAINT fkk33yf1kl67o905gn1y2e4tfad FOREIGN KEY (exercise_id) REFERENCES public.exercise(id);


--
-- Name: user_answer fkknoqpwe1gudlno4bww99ryao7; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.user_answer
    ADD CONSTRAINT fkknoqpwe1gudlno4bww99ryao7 FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: student_exercise fkkucvxp1y3n7dcvxih7xh41vqq; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.student_exercise
    ADD CONSTRAINT fkkucvxp1y3n7dcvxih7xh41vqq FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: course fkkyes7515s3ypoovxrput029bh; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT fkkyes7515s3ypoovxrput029bh FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- Name: payment fklouu98csyullos9k25tbpk4va; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT fklouu98csyullos9k25tbpk4va FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- Name: quiz fkmlx8xst0rbhivy6wu01xn9mb0; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.quiz
    ADD CONSTRAINT fkmlx8xst0rbhivy6wu01xn9mb0 FOREIGN KEY (section_id) REFERENCES public.section(id);


--
-- Name: course_promotion fkoi7ddcbju1s6btmnlofx0l6a7; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.course_promotion
    ADD CONSTRAINT fkoi7ddcbju1s6btmnlofx0l6a7 FOREIGN KEY (promotion_id) REFERENCES public.promotion(id);


--
-- Name: course fkokaxyfpv8p583w8yspapfb2ar; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT fkokaxyfpv8p583w8yspapfb2ar FOREIGN KEY (topic_id) REFERENCES public.topic(id);


--
-- Name: question_lecture fkonxrxh2xe60tetpmc50sv871r; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.question_lecture
    ADD CONSTRAINT fkonxrxh2xe60tetpmc50sv871r FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: meeting fkoo1t29c3u4o1hvo0ge7txtvio; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT fkoo1t29c3u4o1hvo0ge7txtvio FOREIGN KEY (classroom_id) REFERENCES public.classroom(id);


--
-- Name: section fkoy8uc0ftpivwopwf5ptwdtar0; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.section
    ADD CONSTRAINT fkoy8uc0ftpivwopwf5ptwdtar0 FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: category_topic fkpdvgdbpfwroe1ejtaq6c7erir; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.category_topic
    ADD CONSTRAINT fkpdvgdbpfwroe1ejtaq6c7erir FOREIGN KEY (topic_id) REFERENCES public.topic(id);


--
-- Name: review fkprox8elgnr8u5wrq1983degk; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.review
    ADD CONSTRAINT fkprox8elgnr8u5wrq1983degk FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: reference_file fkpspwhgl53mxk628pexuvfgnr0; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.reference_file
    ADD CONSTRAINT fkpspwhgl53mxk628pexuvfgnr0 FOREIGN KEY (reference_id) REFERENCES public.reference(id);


--
-- Name: reference fkr4j1tfs8s8pxvw5h7sqhcpfj5; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.reference
    ADD CONSTRAINT fkr4j1tfs8s8pxvw5h7sqhcpfj5 FOREIGN KEY (classroom_id) REFERENCES public.classroom(id);


--
-- Name: course_promotion fkr63raysugl6xwclvlsk7ojnmb; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.course_promotion
    ADD CONSTRAINT fkr63raysugl6xwclvlsk7ojnmb FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: student_answer fkr8wd05u8yc3ocudxugs5bk50v; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.student_answer
    ADD CONSTRAINT fkr8wd05u8yc3ocudxugs5bk50v FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: order_detail fkrws2q0si6oyd6il8gqe2aennc; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.order_detail
    ADD CONSTRAINT fkrws2q0si6oyd6il8gqe2aennc FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- Name: learning_course fkstu4uf65ac3tat16m7me11wi9; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.learning_course
    ADD CONSTRAINT fkstu4uf65ac3tat16m7me11wi9 FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: learning_quiz fksvxitc881hfk28xpj89vl942y; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.learning_quiz
    ADD CONSTRAINT fksvxitc881hfk28xpj89vl942y FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: user_answer fkt40ylpus0q8p5lwg4fwdu4n1n; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.user_answer
    ADD CONSTRAINT fkt40ylpus0q8p5lwg4fwdu4n1n FOREIGN KEY (question_lecture_id) REFERENCES public.question_lecture(id);


--
-- PostgreSQL database dump complete
--