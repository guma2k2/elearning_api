PGDMP      *                |         	   elearning    16.1    16.0 n    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    27753 	   elearning    DATABASE     k   CREATE DATABASE elearning WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'C';
    DROP DATABASE elearning;
                postgres    false            �            1259    27754    answer    TABLE     j  CREATE TABLE public.answer (
    correct boolean NOT NULL,
    created_at timestamp(6) without time zone,
    id bigint NOT NULL,
    question_id bigint,
    updated_at timestamp(6) without time zone,
    answer_text character varying(255),
    created_by character varying(255),
    last_modified_by character varying(255),
    reason character varying(255)
);
    DROP TABLE public.answer;
       public         heap    postgres    false            �            1259    27759    answer_id_seq    SEQUENCE     v   CREATE SEQUENCE public.answer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.answer_id_seq;
       public          postgres    false    215            �           0    0    answer_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.answer_id_seq OWNED BY public.answer.id;
          public          postgres    false    216            �            1259    27760    cart    TABLE     �   CREATE TABLE public.cart (
    course_id bigint,
    id bigint NOT NULL,
    student_id bigint,
    buy_later boolean NOT NULL
);
    DROP TABLE public.cart;
       public         heap    postgres    false            �            1259    27763    cart_id_seq    SEQUENCE     t   CREATE SEQUENCE public.cart_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.cart_id_seq;
       public          postgres    false    217            �           0    0    cart_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.cart_id_seq OWNED BY public.cart.id;
          public          postgres    false    218            �            1259    27764    category    TABLE     i  CREATE TABLE public.category (
    id integer NOT NULL,
    parent_id integer,
    publish boolean NOT NULL,
    created_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    name character varying(40),
    created_by character varying(255),
    description character varying(255),
    last_modified_by character varying(255)
);
    DROP TABLE public.category;
       public         heap    postgres    false            �            1259    27769    category_id_seq    SEQUENCE     �   CREATE SEQUENCE public.category_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.category_id_seq;
       public          postgres    false    219            �           0    0    category_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.category_id_seq OWNED BY public.category.id;
          public          postgres    false    220            �            1259    27770    category_topic    TABLE     h   CREATE TABLE public.category_topic (
    category_id integer NOT NULL,
    topic_id integer NOT NULL
);
 "   DROP TABLE public.category_topic;
       public         heap    postgres    false            �            1259    27773    course    TABLE     �  CREATE TABLE public.course (
    category_id integer,
    free boolean NOT NULL,
    publish boolean NOT NULL,
    topic_id integer,
    created_at timestamp(6) without time zone,
    id bigint NOT NULL,
    updated_at timestamp(6) without time zone,
    user_id bigint,
    title character varying(60) NOT NULL,
    created_by character varying(255),
    description character varying(255),
    headline character varying(255),
    image_id character varying(255),
    last_modified_by character varying(255),
    level character varying(255),
    objectives character varying(255)[],
    requirements character varying(255)[],
    target_audiences character varying(255)[],
    CONSTRAINT course_level_check CHECK (((level)::text = ANY (ARRAY[('Beginner'::character varying)::text, ('Intermediate'::character varying)::text, ('Expert'::character varying)::text, ('AllLevel'::character varying)::text])))
);
    DROP TABLE public.course;
       public         heap    postgres    false            �            1259    27779    course_id_seq    SEQUENCE     v   CREATE SEQUENCE public.course_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.course_id_seq;
       public          postgres    false    222            �           0    0    course_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.course_id_seq OWNED BY public.course.id;
          public          postgres    false    223            �            1259    27780    lecture    TABLE     �  CREATE TABLE public.lecture (
    duration real NOT NULL,
    number real NOT NULL,
    created_at timestamp(6) without time zone,
    id bigint NOT NULL,
    section_id bigint,
    updated_at timestamp(6) without time zone,
    title character varying(80) NOT NULL,
    created_by character varying(255),
    last_modified_by character varying(255),
    lecture_details character varying(255),
    video_id character varying(255)
);
    DROP TABLE public.lecture;
       public         heap    postgres    false            �            1259    27785    lecture_id_seq    SEQUENCE     w   CREATE SEQUENCE public.lecture_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.lecture_id_seq;
       public          postgres    false    224            �           0    0    lecture_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.lecture_id_seq OWNED BY public.lecture.id;
          public          postgres    false    225            �            1259    27786    question    TABLE     !  CREATE TABLE public.question (
    created_at timestamp(6) without time zone,
    id bigint NOT NULL,
    quiz_id bigint,
    updated_at timestamp(6) without time zone,
    created_by character varying(255),
    last_modified_by character varying(255),
    title character varying(255)
);
    DROP TABLE public.question;
       public         heap    postgres    false            �            1259    27791    question_id_seq    SEQUENCE     x   CREATE SEQUENCE public.question_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.question_id_seq;
       public          postgres    false    226            �           0    0    question_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.question_id_seq OWNED BY public.question.id;
          public          postgres    false    227            �            1259    27792    quiz    TABLE     b  CREATE TABLE public.quiz (
    number real NOT NULL,
    created_at timestamp(6) without time zone,
    id bigint NOT NULL,
    section_id bigint,
    updated_at timestamp(6) without time zone,
    created_by character varying(255),
    description character varying(255),
    last_modified_by character varying(255),
    title character varying(255)
);
    DROP TABLE public.quiz;
       public         heap    postgres    false            �            1259    27797    quiz_id_seq    SEQUENCE     t   CREATE SEQUENCE public.quiz_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.quiz_id_seq;
       public          postgres    false    228            �           0    0    quiz_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.quiz_id_seq OWNED BY public.quiz.id;
          public          postgres    false    229            �            1259    27798    review    TABLE       CREATE TABLE public.review (
    rating_star integer NOT NULL,
    course_id bigint,
    created_at timestamp(6) without time zone,
    id bigint NOT NULL,
    student_id bigint,
    updated_at timestamp(6) without time zone,
    content character varying(255)
);
    DROP TABLE public.review;
       public         heap    postgres    false            �            1259    27801    review_id_seq    SEQUENCE     v   CREATE SEQUENCE public.review_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.review_id_seq;
       public          postgres    false    230            �           0    0    review_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.review_id_seq OWNED BY public.review.id;
          public          postgres    false    231            �            1259    27802    section    TABLE     b  CREATE TABLE public.section (
    number real NOT NULL,
    course_id bigint,
    created_at timestamp(6) without time zone,
    id bigint NOT NULL,
    updated_at timestamp(6) without time zone,
    created_by character varying(255),
    last_modified_by character varying(255),
    objective character varying(255),
    title character varying(255)
);
    DROP TABLE public.section;
       public         heap    postgres    false            �            1259    27807    section_id_seq    SEQUENCE     w   CREATE SEQUENCE public.section_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.section_id_seq;
       public          postgres    false    232            �           0    0    section_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.section_id_seq OWNED BY public.section.id;
          public          postgres    false    233            �            1259    27808    sudent    TABLE     �  CREATE TABLE public.sudent (
    active boolean NOT NULL,
    date_of_birth date,
    created_at timestamp(6) without time zone,
    id bigint NOT NULL,
    updated_at timestamp(6) without time zone,
    first_name character varying(20),
    last_name character varying(20),
    email character varying(50),
    created_by character varying(255),
    gender character varying(255),
    last_modified_by character varying(255),
    password character varying(255),
    photo character varying(255),
    CONSTRAINT sudent_gender_check CHECK (((gender)::text = ANY (ARRAY[('MALE'::character varying)::text, ('FEMALE'::character varying)::text])))
);
    DROP TABLE public.sudent;
       public         heap    postgres    false            �            1259    27814    sudent_id_seq    SEQUENCE     v   CREATE SEQUENCE public.sudent_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.sudent_id_seq;
       public          postgres    false    234            �           0    0    sudent_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.sudent_id_seq OWNED BY public.sudent.id;
          public          postgres    false    235            �            1259    27815    topic    TABLE     P  CREATE TABLE public.topic (
    id integer NOT NULL,
    publish boolean NOT NULL,
    created_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    created_by character varying(255),
    description character varying(255),
    last_modified_by character varying(255),
    name character varying(255)
);
    DROP TABLE public.topic;
       public         heap    postgres    false            �            1259    27820    topic_id_seq    SEQUENCE     �   CREATE SEQUENCE public.topic_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.topic_id_seq;
       public          postgres    false    236            �           0    0    topic_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.topic_id_seq OWNED BY public.topic.id;
          public          postgres    false    237            �            1259    27821    user    TABLE     m  CREATE TABLE public."user" (
    active boolean NOT NULL,
    date_of_birth date,
    created_at timestamp(6) without time zone,
    id bigint NOT NULL,
    updated_at timestamp(6) without time zone,
    first_name character varying(20),
    last_name character varying(20),
    email character varying(50),
    created_by character varying(255),
    gender character varying(255),
    last_modified_by character varying(255),
    password character varying(255),
    photo character varying(255),
    role character varying(255),
    CONSTRAINT user_gender_check CHECK (((gender)::text = ANY (ARRAY[('MALE'::character varying)::text, ('FEMALE'::character varying)::text]))),
    CONSTRAINT user_role_check CHECK (((role)::text = ANY (ARRAY[('ROLE_ADMIN'::character varying)::text, ('ROLE_INSTRUCTOR'::character varying)::text, ('ROLE_STUDENT'::character varying)::text])))
);
    DROP TABLE public."user";
       public         heap    postgres    false            �            1259    27828    user_id_seq    SEQUENCE     t   CREATE SEQUENCE public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.user_id_seq;
       public          postgres    false    238            �           0    0    user_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.user_id_seq OWNED BY public."user".id;
          public          postgres    false    239            �           2604    27829 	   answer id    DEFAULT     f   ALTER TABLE ONLY public.answer ALTER COLUMN id SET DEFAULT nextval('public.answer_id_seq'::regclass);
 8   ALTER TABLE public.answer ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    216    215            �           2604    27830    cart id    DEFAULT     b   ALTER TABLE ONLY public.cart ALTER COLUMN id SET DEFAULT nextval('public.cart_id_seq'::regclass);
 6   ALTER TABLE public.cart ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    218    217            �           2604    27831    category id    DEFAULT     j   ALTER TABLE ONLY public.category ALTER COLUMN id SET DEFAULT nextval('public.category_id_seq'::regclass);
 :   ALTER TABLE public.category ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    220    219            �           2604    27832 	   course id    DEFAULT     f   ALTER TABLE ONLY public.course ALTER COLUMN id SET DEFAULT nextval('public.course_id_seq'::regclass);
 8   ALTER TABLE public.course ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    223    222            �           2604    27833 
   lecture id    DEFAULT     h   ALTER TABLE ONLY public.lecture ALTER COLUMN id SET DEFAULT nextval('public.lecture_id_seq'::regclass);
 9   ALTER TABLE public.lecture ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    225    224            �           2604    27834    question id    DEFAULT     j   ALTER TABLE ONLY public.question ALTER COLUMN id SET DEFAULT nextval('public.question_id_seq'::regclass);
 :   ALTER TABLE public.question ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    227    226            �           2604    27835    quiz id    DEFAULT     b   ALTER TABLE ONLY public.quiz ALTER COLUMN id SET DEFAULT nextval('public.quiz_id_seq'::regclass);
 6   ALTER TABLE public.quiz ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    229    228            �           2604    27836 	   review id    DEFAULT     f   ALTER TABLE ONLY public.review ALTER COLUMN id SET DEFAULT nextval('public.review_id_seq'::regclass);
 8   ALTER TABLE public.review ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    231    230            �           2604    27837 
   section id    DEFAULT     h   ALTER TABLE ONLY public.section ALTER COLUMN id SET DEFAULT nextval('public.section_id_seq'::regclass);
 9   ALTER TABLE public.section ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    233    232            �           2604    27838 	   sudent id    DEFAULT     f   ALTER TABLE ONLY public.sudent ALTER COLUMN id SET DEFAULT nextval('public.sudent_id_seq'::regclass);
 8   ALTER TABLE public.sudent ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    235    234            �           2604    27839    topic id    DEFAULT     d   ALTER TABLE ONLY public.topic ALTER COLUMN id SET DEFAULT nextval('public.topic_id_seq'::regclass);
 7   ALTER TABLE public.topic ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    237    236            �           2604    27840    user id    DEFAULT     d   ALTER TABLE ONLY public."user" ALTER COLUMN id SET DEFAULT nextval('public.user_id_seq'::regclass);
 8   ALTER TABLE public."user" ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    239    238            q          0    27754    answer 
   TABLE DATA           �   COPY public.answer (correct, created_at, id, question_id, updated_at, answer_text, created_by, last_modified_by, reason) FROM stdin;
    public          postgres    false    215   �       s          0    27760    cart 
   TABLE DATA           D   COPY public.cart (course_id, id, student_id, buy_later) FROM stdin;
    public          postgres    false    217   ��       u          0    27764    category 
   TABLE DATA           �   COPY public.category (id, parent_id, publish, created_at, updated_at, name, created_by, description, last_modified_by) FROM stdin;
    public          postgres    false    219   ��       w          0    27770    category_topic 
   TABLE DATA           ?   COPY public.category_topic (category_id, topic_id) FROM stdin;
    public          postgres    false    221   ��       x          0    27773    course 
   TABLE DATA           �   COPY public.course (category_id, free, publish, topic_id, created_at, id, updated_at, user_id, title, created_by, description, headline, image_id, last_modified_by, level, objectives, requirements, target_audiences) FROM stdin;
    public          postgres    false    222   $�       z          0    27780    lecture 
   TABLE DATA           �   COPY public.lecture (duration, number, created_at, id, section_id, updated_at, title, created_by, last_modified_by, lecture_details, video_id) FROM stdin;
    public          postgres    false    224   ��       |          0    27786    question 
   TABLE DATA           l   COPY public.question (created_at, id, quiz_id, updated_at, created_by, last_modified_by, title) FROM stdin;
    public          postgres    false    226   ��       ~          0    27792    quiz 
   TABLE DATA           �   COPY public.quiz (number, created_at, id, section_id, updated_at, created_by, description, last_modified_by, title) FROM stdin;
    public          postgres    false    228   ��       �          0    27798    review 
   TABLE DATA           i   COPY public.review (rating_star, course_id, created_at, id, student_id, updated_at, content) FROM stdin;
    public          postgres    false    230   )�       �          0    27802    section 
   TABLE DATA           �   COPY public.section (number, course_id, created_at, id, updated_at, created_by, last_modified_by, objective, title) FROM stdin;
    public          postgres    false    232   F�       �          0    27808    sudent 
   TABLE DATA           �   COPY public.sudent (active, date_of_birth, created_at, id, updated_at, first_name, last_name, email, created_by, gender, last_modified_by, password, photo) FROM stdin;
    public          postgres    false    234   ��       �          0    27815    topic 
   TABLE DATA           u   COPY public.topic (id, publish, created_at, updated_at, created_by, description, last_modified_by, name) FROM stdin;
    public          postgres    false    236   �       �          0    27821    user 
   TABLE DATA           �   COPY public."user" (active, date_of_birth, created_at, id, updated_at, first_name, last_name, email, created_by, gender, last_modified_by, password, photo, role) FROM stdin;
    public          postgres    false    238   ː       �           0    0    answer_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.answer_id_seq', 1, false);
          public          postgres    false    216            �           0    0    cart_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.cart_id_seq', 1, false);
          public          postgres    false    218            �           0    0    category_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.category_id_seq', 17, true);
          public          postgres    false    220            �           0    0    course_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.course_id_seq', 15, true);
          public          postgres    false    223            �           0    0    lecture_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.lecture_id_seq', 8, true);
          public          postgres    false    225            �           0    0    question_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.question_id_seq', 1, false);
          public          postgres    false    227            �           0    0    quiz_id_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('public.quiz_id_seq', 3, true);
          public          postgres    false    229            �           0    0    review_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.review_id_seq', 1, false);
          public          postgres    false    231            �           0    0    section_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.section_id_seq', 5, true);
          public          postgres    false    233            �           0    0    sudent_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.sudent_id_seq', 1, false);
          public          postgres    false    235            �           0    0    topic_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.topic_id_seq', 5, true);
          public          postgres    false    237            �           0    0    user_id_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('public.user_id_seq', 1, true);
          public          postgres    false    239            �           2606    27842    answer answer_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.answer
    ADD CONSTRAINT answer_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.answer DROP CONSTRAINT answer_pkey;
       public            postgres    false    215            �           2606    27844    cart cart_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.cart
    ADD CONSTRAINT cart_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.cart DROP CONSTRAINT cart_pkey;
       public            postgres    false    217            �           2606    27846    category category_name_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_name_key UNIQUE (name);
 D   ALTER TABLE ONLY public.category DROP CONSTRAINT category_name_key;
       public            postgres    false    219            �           2606    27848    category category_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.category DROP CONSTRAINT category_pkey;
       public            postgres    false    219            �           2606    27850 "   category_topic category_topic_pkey 
   CONSTRAINT     s   ALTER TABLE ONLY public.category_topic
    ADD CONSTRAINT category_topic_pkey PRIMARY KEY (category_id, topic_id);
 L   ALTER TABLE ONLY public.category_topic DROP CONSTRAINT category_topic_pkey;
       public            postgres    false    221    221            �           2606    27852    course course_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.course DROP CONSTRAINT course_pkey;
       public            postgres    false    222            �           2606    27854    course course_title_key 
   CONSTRAINT     S   ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_title_key UNIQUE (title);
 A   ALTER TABLE ONLY public.course DROP CONSTRAINT course_title_key;
       public            postgres    false    222            �           2606    27856    lecture lecture_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.lecture
    ADD CONSTRAINT lecture_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.lecture DROP CONSTRAINT lecture_pkey;
       public            postgres    false    224            �           2606    27858    question question_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.question
    ADD CONSTRAINT question_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.question DROP CONSTRAINT question_pkey;
       public            postgres    false    226            �           2606    27860    quiz quiz_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.quiz
    ADD CONSTRAINT quiz_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.quiz DROP CONSTRAINT quiz_pkey;
       public            postgres    false    228            �           2606    27862    review review_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.review
    ADD CONSTRAINT review_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.review DROP CONSTRAINT review_pkey;
       public            postgres    false    230            �           2606    27864    section section_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.section
    ADD CONSTRAINT section_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.section DROP CONSTRAINT section_pkey;
       public            postgres    false    232            �           2606    27866    sudent sudent_email_key 
   CONSTRAINT     S   ALTER TABLE ONLY public.sudent
    ADD CONSTRAINT sudent_email_key UNIQUE (email);
 A   ALTER TABLE ONLY public.sudent DROP CONSTRAINT sudent_email_key;
       public            postgres    false    234            �           2606    27868    sudent sudent_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.sudent
    ADD CONSTRAINT sudent_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.sudent DROP CONSTRAINT sudent_pkey;
       public            postgres    false    234            �           2606    27870    topic topic_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.topic
    ADD CONSTRAINT topic_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.topic DROP CONSTRAINT topic_pkey;
       public            postgres    false    236            �           2606    27872    user user_email_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_email_key UNIQUE (email);
 ?   ALTER TABLE ONLY public."user" DROP CONSTRAINT user_email_key;
       public            postgres    false    238            �           2606    27874    user user_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public."user" DROP CONSTRAINT user_pkey;
       public            postgres    false    238            �           2606    27875 $   category fk2y94svpmqttx80mshyny85wqr    FK CONSTRAINT     �   ALTER TABLE ONLY public.category
    ADD CONSTRAINT fk2y94svpmqttx80mshyny85wqr FOREIGN KEY (parent_id) REFERENCES public.category(id);
 N   ALTER TABLE ONLY public.category DROP CONSTRAINT fk2y94svpmqttx80mshyny85wqr;
       public          postgres    false    219    219    3512            �           2606    27880 #   lecture fk568elaju5okd8k0hukt18mtk7    FK CONSTRAINT     �   ALTER TABLE ONLY public.lecture
    ADD CONSTRAINT fk568elaju5okd8k0hukt18mtk7 FOREIGN KEY (section_id) REFERENCES public.section(id);
 M   ALTER TABLE ONLY public.lecture DROP CONSTRAINT fk568elaju5okd8k0hukt18mtk7;
       public          postgres    false    224    3528    232            �           2606    27885 *   category_topic fk7bbgepmfwmj5nlvvjmp91lqtv    FK CONSTRAINT     �   ALTER TABLE ONLY public.category_topic
    ADD CONSTRAINT fk7bbgepmfwmj5nlvvjmp91lqtv FOREIGN KEY (category_id) REFERENCES public.category(id);
 T   ALTER TABLE ONLY public.category_topic DROP CONSTRAINT fk7bbgepmfwmj5nlvvjmp91lqtv;
       public          postgres    false    219    3512    221            �           2606    27890 "   answer fk8frr4bcabmmeyyu60qt7iiblo    FK CONSTRAINT     �   ALTER TABLE ONLY public.answer
    ADD CONSTRAINT fk8frr4bcabmmeyyu60qt7iiblo FOREIGN KEY (question_id) REFERENCES public.question(id);
 L   ALTER TABLE ONLY public.answer DROP CONSTRAINT fk8frr4bcabmmeyyu60qt7iiblo;
       public          postgres    false    3522    226    215            �           2606    27895 $   question fkb0yh0c1qaxfwlcnwo9dms2txf    FK CONSTRAINT     �   ALTER TABLE ONLY public.question
    ADD CONSTRAINT fkb0yh0c1qaxfwlcnwo9dms2txf FOREIGN KEY (quiz_id) REFERENCES public.quiz(id);
 N   ALTER TABLE ONLY public.question DROP CONSTRAINT fkb0yh0c1qaxfwlcnwo9dms2txf;
       public          postgres    false    226    3524    228            �           2606    27900     cart fke8qhvp3rieyui6fxssjs4r34r    FK CONSTRAINT     �   ALTER TABLE ONLY public.cart
    ADD CONSTRAINT fke8qhvp3rieyui6fxssjs4r34r FOREIGN KEY (course_id) REFERENCES public.course(id);
 J   ALTER TABLE ONLY public.cart DROP CONSTRAINT fke8qhvp3rieyui6fxssjs4r34r;
       public          postgres    false    217    222    3516            �           2606    27905 "   course fkg4vkpa3s2ck9iipvl3lanpue7    FK CONSTRAINT     �   ALTER TABLE ONLY public.course
    ADD CONSTRAINT fkg4vkpa3s2ck9iipvl3lanpue7 FOREIGN KEY (user_id) REFERENCES public."user"(id);
 L   ALTER TABLE ONLY public.course DROP CONSTRAINT fkg4vkpa3s2ck9iipvl3lanpue7;
       public          postgres    false    222    238    3538            �           2606    27910 "   course fkkyes7515s3ypoovxrput029bh    FK CONSTRAINT     �   ALTER TABLE ONLY public.course
    ADD CONSTRAINT fkkyes7515s3ypoovxrput029bh FOREIGN KEY (category_id) REFERENCES public.category(id);
 L   ALTER TABLE ONLY public.course DROP CONSTRAINT fkkyes7515s3ypoovxrput029bh;
       public          postgres    false    219    3512    222            �           2606    27915 "   review fklrd8ux9urb210uts0x9jfmvov    FK CONSTRAINT     �   ALTER TABLE ONLY public.review
    ADD CONSTRAINT fklrd8ux9urb210uts0x9jfmvov FOREIGN KEY (student_id) REFERENCES public.sudent(id);
 L   ALTER TABLE ONLY public.review DROP CONSTRAINT fklrd8ux9urb210uts0x9jfmvov;
       public          postgres    false    234    230    3532            �           2606    27920     quiz fkmlx8xst0rbhivy6wu01xn9mb0    FK CONSTRAINT     �   ALTER TABLE ONLY public.quiz
    ADD CONSTRAINT fkmlx8xst0rbhivy6wu01xn9mb0 FOREIGN KEY (section_id) REFERENCES public.section(id);
 J   ALTER TABLE ONLY public.quiz DROP CONSTRAINT fkmlx8xst0rbhivy6wu01xn9mb0;
       public          postgres    false    232    228    3528            �           2606    27925 "   course fkokaxyfpv8p583w8yspapfb2ar    FK CONSTRAINT     �   ALTER TABLE ONLY public.course
    ADD CONSTRAINT fkokaxyfpv8p583w8yspapfb2ar FOREIGN KEY (topic_id) REFERENCES public.topic(id);
 L   ALTER TABLE ONLY public.course DROP CONSTRAINT fkokaxyfpv8p583w8yspapfb2ar;
       public          postgres    false    236    222    3534            �           2606    27930 #   section fkoy8uc0ftpivwopwf5ptwdtar0    FK CONSTRAINT     �   ALTER TABLE ONLY public.section
    ADD CONSTRAINT fkoy8uc0ftpivwopwf5ptwdtar0 FOREIGN KEY (course_id) REFERENCES public.course(id);
 M   ALTER TABLE ONLY public.section DROP CONSTRAINT fkoy8uc0ftpivwopwf5ptwdtar0;
       public          postgres    false    3516    222    232            �           2606    27935 *   category_topic fkpdvgdbpfwroe1ejtaq6c7erir    FK CONSTRAINT     �   ALTER TABLE ONLY public.category_topic
    ADD CONSTRAINT fkpdvgdbpfwroe1ejtaq6c7erir FOREIGN KEY (topic_id) REFERENCES public.topic(id);
 T   ALTER TABLE ONLY public.category_topic DROP CONSTRAINT fkpdvgdbpfwroe1ejtaq6c7erir;
       public          postgres    false    3534    221    236            �           2606    27940 !   review fkprox8elgnr8u5wrq1983degk    FK CONSTRAINT     �   ALTER TABLE ONLY public.review
    ADD CONSTRAINT fkprox8elgnr8u5wrq1983degk FOREIGN KEY (course_id) REFERENCES public.course(id);
 K   ALTER TABLE ONLY public.review DROP CONSTRAINT fkprox8elgnr8u5wrq1983degk;
       public          postgres    false    230    3516    222            �           2606    27945     cart fkt6nnel0amh3xeip0esvs91238    FK CONSTRAINT     �   ALTER TABLE ONLY public.cart
    ADD CONSTRAINT fkt6nnel0amh3xeip0esvs91238 FOREIGN KEY (student_id) REFERENCES public.sudent(id);
 J   ALTER TABLE ONLY public.cart DROP CONSTRAINT fkt6nnel0amh3xeip0esvs91238;
       public          postgres    false    234    217    3532            q      x������ � �      s      x������ � �      u   ,  x���?k�@�g�Sܔ-�ݽ�;��,��K�,�b,�Z
�\ȘfJ���n)!�Х�5t�~��&}���Y����{���.�Q	&�1S���i������Xw('������m
2s��,eq3+��w��<ZT�I1���^"O�`\ĢK���[�)�C`����1��$�iF
t��BP� �'TI�@vI�]W�u�]B�ЀQƤ�V�����U9,��:��@9H-<9|�v5!Yn��x�+�J�����H�6]�^FIf�XJ�#��x��rM�d��]��p>U�K�$s�	�*6��)�u�cF�����0��ȕ��+go�U�4���2ٔN��X�����L�� ��H�����7�3�C$~�+r�6?K\8��*+�X�$������n�vn�X�gIC��c�Uz��{��v���v�#��;��]��"��lG��<��@;�edޏ.�x��'�J����)��吠�/ٶq���_a�+cptI�F_Zy;8��+����־(Lʀ��:i�oS�ퟂ�ql�k��>�������:S�      w      x�34�4�24�4� �D�r��qqq @U       x   S  x���Mj�0��|�^ �iF��!�jw�P��?����׮V6A�a�^0���n?^�<z^a\|"�0v!B���/��J��x:����:�.n�<����<tm�yx_�p���~{��th���2�ZV��	[�RL7{JqC_���w�=?0[��oj��/�l�����<�P���0s"�B�K1���x`BAqR��,��5eq��9V`�p|�>I.�b6�J�QE��B�b�D���$��R�f��L�,���b��I�J��L��kVװ��KUy��-�p^���-]����r�5�kX��ŀ�����kVװ��+��0��r�5��w��4���ݖ      z   �   x���Mn�0F��)� ��3�\�Yu�]6DqV�+P[q�8�&�
!>{c=�O�����t�Z�hldV^|6�)u]����]��0��g��Ҹ8�?ˮ��D��Y�ɂ��OY���*Q+C�H�-����2�:��q�C:f$]
(q�.k�%�,���#��`<ص���m�dلMDV��@V�7|y�P�h���8����JvI���r�Vi*����$o��K��g�gUU��n��      |      x������ � �      ~   |   x���1�0�W������hR�@��R��2F2��j�Y!t�8�K!�h'CRr�v�n���u��Aͼ��q�F�>#ZI�H{���n�fL��z�?�}�]�R��,3��K�      �      x������ � �      �   �   x���1�0Eg�\�(�c9@;�S��DT��HD�}�����b=�?}+@��������s���2QÐ�~	���x�㺻�+�oB��V2������<��
S��G��2�InXS:�Ӽ�W�ûB�Qv���,�>M*����J�-��c��cZ�K!��4�E      �      x������ � �      �   �   x��б
�0���)�7�&1��"E��K(E:��&
����!d=����U�I5h�Z�e�� �ʙ\�˺����aܪ�i����y��Z��Q�i�K�B���S�@CJ�\*pΣb���R
[�%C�T�.�#蔠��3'���a��p �~J�      �   �   x�}��k�0 ���_�C��m��4���B�8d0B�J��%����n;���}�x0B����&�L!'4�&i�P�̧�3@��A�� S�_�����Xw�iQe; �5���������O3S?�s>�B'��!���Pgپ��W>������:���]Wt+�O�Cp>�xN���/+k�2���Bz<�g�2,��R4P6�@�$����A��.?/�f��(�~ JmSi     