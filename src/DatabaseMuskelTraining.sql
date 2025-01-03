PGDMP      !                 }            test    17.1    17.1                0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false                       1262    16388    test    DATABASE     x   CREATE DATABASE test WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'German_Germany.1252';
    DROP DATABASE test;
                     postgres    false            �            1259    24621    muskeln    TABLE     c   CREATE TABLE public.muskeln (
    id integer NOT NULL,
    name character varying(100) NOT NULL
);
    DROP TABLE public.muskeln;
       public         heap r       postgres    false            �            1259    24620    muskeln_id_seq    SEQUENCE     �   CREATE SEQUENCE public.muskeln_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.muskeln_id_seq;
       public               postgres    false    220                       0    0    muskeln_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.muskeln_id_seq OWNED BY public.muskeln.id;
          public               postgres    false    219            �            1259    24636    muskeln_uebungen    TABLE     i   CREATE TABLE public.muskeln_uebungen (
    muskel_id integer NOT NULL,
    uebung_id integer NOT NULL
);
 $   DROP TABLE public.muskeln_uebungen;
       public         heap r       postgres    false            �            1259    24628    uebungen    TABLE     z   CREATE TABLE public.uebungen (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    description text
);
    DROP TABLE public.uebungen;
       public         heap r       postgres    false            �            1259    24627    uebungen_id_seq    SEQUENCE     �   CREATE SEQUENCE public.uebungen_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.uebungen_id_seq;
       public               postgres    false    222                       0    0    uebungen_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.uebungen_id_seq OWNED BY public.uebungen.id;
          public               postgres    false    221            �            1259    24581    users    TABLE     �   CREATE TABLE public.users (
    id integer NOT NULL,
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL
);
    DROP TABLE public.users;
       public         heap r       postgres    false            �            1259    24580    users_id_seq    SEQUENCE     �   CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.users_id_seq;
       public               postgres    false    218                       0    0    users_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;
          public               postgres    false    217            f           2604    24624 
   muskeln id    DEFAULT     h   ALTER TABLE ONLY public.muskeln ALTER COLUMN id SET DEFAULT nextval('public.muskeln_id_seq'::regclass);
 9   ALTER TABLE public.muskeln ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    219    220    220            g           2604    24631    uebungen id    DEFAULT     j   ALTER TABLE ONLY public.uebungen ALTER COLUMN id SET DEFAULT nextval('public.uebungen_id_seq'::regclass);
 :   ALTER TABLE public.uebungen ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    221    222    222            e           2604    24584    users id    DEFAULT     d   ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);
 7   ALTER TABLE public.users ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    217    218    218                      0    24621    muskeln 
   TABLE DATA           +   COPY public.muskeln (id, name) FROM stdin;
    public               postgres    false    220           	          0    24636    muskeln_uebungen 
   TABLE DATA           @   COPY public.muskeln_uebungen (muskel_id, uebung_id) FROM stdin;
    public               postgres    false    223   "                 0    24628    uebungen 
   TABLE DATA           9   COPY public.uebungen (id, name, description) FROM stdin;
    public               postgres    false    222   �#                 0    24581    users 
   TABLE DATA           7   COPY public.users (id, username, password) FROM stdin;
    public               postgres    false    218   *                  0    0    muskeln_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.muskeln_id_seq', 51, true);
          public               postgres    false    219                       0    0    uebungen_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.uebungen_id_seq', 107, true);
          public               postgres    false    221                       0    0    users_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.users_id_seq', 3, true);
          public               postgres    false    217            k           2606    24626    muskeln muskeln_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.muskeln
    ADD CONSTRAINT muskeln_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.muskeln DROP CONSTRAINT muskeln_pkey;
       public                 postgres    false    220            o           2606    24640 &   muskeln_uebungen muskeln_uebungen_pkey 
   CONSTRAINT     v   ALTER TABLE ONLY public.muskeln_uebungen
    ADD CONSTRAINT muskeln_uebungen_pkey PRIMARY KEY (muskel_id, uebung_id);
 P   ALTER TABLE ONLY public.muskeln_uebungen DROP CONSTRAINT muskeln_uebungen_pkey;
       public                 postgres    false    223    223            m           2606    24635    uebungen uebungen_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.uebungen
    ADD CONSTRAINT uebungen_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.uebungen DROP CONSTRAINT uebungen_pkey;
       public                 postgres    false    222            i           2606    24588    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public                 postgres    false    218            p           2606    24641 0   muskeln_uebungen muskeln_uebungen_muskel_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.muskeln_uebungen
    ADD CONSTRAINT muskeln_uebungen_muskel_id_fkey FOREIGN KEY (muskel_id) REFERENCES public.muskeln(id) ON DELETE CASCADE;
 Z   ALTER TABLE ONLY public.muskeln_uebungen DROP CONSTRAINT muskeln_uebungen_muskel_id_fkey;
       public               postgres    false    223    4715    220            q           2606    24646 0   muskeln_uebungen muskeln_uebungen_uebung_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.muskeln_uebungen
    ADD CONSTRAINT muskeln_uebungen_uebung_id_fkey FOREIGN KEY (uebung_id) REFERENCES public.uebungen(id) ON DELETE CASCADE;
 Z   ALTER TABLE ONLY public.muskeln_uebungen DROP CONSTRAINT muskeln_uebungen_uebung_id_fkey;
       public               postgres    false    4717    222    223               �  x�]�Kr�0���)t��%J~,�I�xƝ�Q�l��%&F#�*(u<9[w�XA���@��#��0�W<���pÃ�U��T�ƪ�X��s����L�ͫ�`-K(����Z�w+���?��=E�La*M��q}�2(�U��T��}C��U*�L�>��׮%K�
�4Co~�#������Y����AK����w4�@RNV6�@n�uc�#L�����G���hr���U�/�,���}���:����8&eG�����GP�-���ڽ�0�L�l�1��PȺ����1C���H=�'�TX�������ч��Ki��}���ӱ�X�m�uޡ�ikӺ�^�3�@�A{Vz9����&���q3�Jט�Ja#~v�5mt�Q�>~>7�븶8���o!.�cE�H^@�,Θ`.���a��/N���$)_�h��5��Єd��
���q+Q��t��t{q�Ezv������J)�>A"�      	   q  x�5��q!�� ������t�`W��f�m���9}���:��t������C �l/���-7�X�U�m^���gr�<�P������/,��m�?�m�L��"��.�)1�2_sF��M%&a�����P��اgyP2�^���=̏��癣m�m{)��'���o���aRǒf
�6��*H��vܵ��4u����Q�璄(8{Q(K|6A�vP��]V���K_��r��(�}A�.Q��S����w`��L��V&k��//mH���M�6��$'�Zލbj���p[�/^�
��R�T�ʑ�(_0�"�� �jϳ{��|��0ټG��������{p��z���~�         �  x��WKr�8]7N�e\%O��$�K�vJN⑜�fj6	S(Q �q��\cV��1��M�$� ��d6�M<4��^���[����)�WYj�	3E���]��0i�7Z��~��Pn�`��-Z�ͧc�״�%#�E�
1X�=� �2���h�$���5/RM&�(��X�ޢ5fu.YZ/��9�K�"!>\�\ՠ������V�IA���;�+ƴ�_h&F��ل�l�t���<gY�D�f}ƣ�2��j�2���Bn�;�f�C�Vq���M�q�2���P%�\�{&c&Ys⸀?��k�����4����.nR�&bV��)f?uA�Q	�%S��T���B��`�ý����Mo�M5���*���,��v��0�W��R�o
u6����H1�ULS[��B�0/�L�T!kȀb��_ȠC]i���"�f��[�fUi����%ë�-S���?��\�/CI�'b��94��p��q=X0�˓�O)G�tX��Ff�B|��;�)�4���1�9Ce(Fo��۫"3�*�I�&q}����<���V�ݜE�P�bg.�:#�R�&�j t��)׻WM� L�l�t�{��`�O��n�����H�����̕V��+�"
�G/Q�h��-Z̬��R#���dYf{.�g6��ߊ0�Y�Cϳ�V8�#�.
���9�k}6�F��`�'�؆-/�lg���3���*.���bj���Ʈ�:�χ�xߪ�J��)��D�݆"��pT�=,G�"v�����./@��qe��i�q%��['��n�,�2)̇�X~�c��)c�;����� ��t&�T�+��?�e.���]��K��Z��Y����C���um�i��&��z$���9�b1�ȇ�0b֒T��}��y�C��&��2�"q�0�Jc��<�'�映���w�?�N���3����g쀉����0���؅+Ԓ�������=�J3��mB���o�d<����)!-�?t�6�1\�˴4�j��Y���)��g�WJ#(����X� '�L0F�!WL��m�,8�Gi_��L�p�b�%�B&\�0Aݤa7���	+_��TQ��L\�Z��I�/���.�Mv�-����H�A�E��Jߊ�;MF��,?���XW�DW�)����sg'���Oe�1CAl
��gi.�)\g�/Ԃ�:Vk

'Q��_+4�t�t:��#k����q�̒GJPv��l��C���V>�c���!��^;9V����4��fv�;/���>�ذ2V�9D�|����/}n�s���5Ku�x�Í_KΌ�zğ�{Qj���,qxJ�3:�[�|t�~��dZ�ɝD8U:�A���S�<��'���?����sɓ��Vw�F�c�5V!>��	|��Bn�����-.�<G}�
7���t�l46;�:	��[�rm��7��)g���|'��ُ#�5�>��#�x[�N;�8��R��P�3h
�*wor&p�a���2�_(�C��2��!Gn`0�P5v�7�֕I��~�&�\��:K���D���gW���ڎ��3���G�h��I��{g��džU$���$��.$n q2���ZU�Te�*�!c�ZAN�jM�t����f�F;�Cr{�?!�����c            x�3�H-I-�H,�N-����� >P]     