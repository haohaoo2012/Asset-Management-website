--
-- TOC entry 200 (class 1259 OID 25705)
-- Name: assets; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.assets
(
    assetcode character varying(255) NOT NULL,
    assetname character varying(255),
    installdate timestamp
    without time zone NOT NULL,
    isdeleted boolean NOT NULL,
    specification character varying
    (255),
    state smallint NOT NULL,
    categorycode character varying
    (255) NOT NULL,
    locationid bigint NOT NULL
);


    --
    -- TOC entry 201 (class 1259 OID 25713)
    -- Name: assignments; Type: TABLE; Schema: public; Owner: -
    --

    CREATE TABLE public.assignments
    (
        assignmentid bigint NOT NULL,
        assigneddate timestamp
        without time zone,
    isdeleted boolean,
    note character varying
        (255),
    state smallint,
    assetcode character varying
        (255) NOT NULL,
    assignedby character varying
        (255) NOT NULL,
    assignedto character varying
        (255) NOT NULL
);


        --
        -- TOC entry 202 (class 1259 OID 25721)
        -- Name: categories; Type: TABLE; Schema: public; Owner: -
        --

        CREATE TABLE public.categories
        (
            categorycode character varying(255) NOT NULL,
            categoryname character varying(255)
        );


        --
        -- TOC entry 207 (class 1259 OID 25777)
        -- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: -
        --

        CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


        --
        -- TOC entry 208 (class 1259 OID 25779)
        -- Name: location_sequence; Type: SEQUENCE; Schema: public; Owner: -
        --

        CREATE SEQUENCE public.location_sequence
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


        --
        -- TOC entry 203 (class 1259 OID 25729)
        -- Name: locations; Type: TABLE; Schema: public; Owner: -
        --

        CREATE TABLE public.locations
        (
            locationid bigint NOT NULL,
            address character varying(255)
        );


        --
        -- TOC entry 204 (class 1259 OID 25734)
        -- Name: requests; Type: TABLE; Schema: public; Owner: -
        --

        CREATE TABLE public.requests
        (
            requestid bigint NOT NULL,
            isdeleted boolean,
            returneddate timestamp
            without time zone,
    state smallint,
    acceptedby character varying
            (255),
    assignmentid bigint NOT NULL,
    requestedby character varying
            (255) NOT NULL
);


            --
            -- TOC entry 205 (class 1259 OID 25742)
            -- Name: roles; Type: TABLE; Schema: public; Owner: -
            --

            CREATE TABLE public.roles
            (
                roleid bigint NOT NULL,
                rolename character varying(255) NOT NULL
            );


            --
            -- TOC entry 209 (class 1259 OID 25781)
            -- Name: staffcode_generator; Type: SEQUENCE; Schema: public; Owner: -
            --

            CREATE SEQUENCE public.staffcode_generator
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


            --
            -- TOC entry 206 (class 1259 OID 25747)
            -- Name: users; Type: TABLE; Schema: public; Owner: -
            --

            CREATE TABLE public.users
            (
                staffcode character varying(255) NOT NULL,
                dateofbirth timestamp
                without time zone,
    firstlogin boolean,
    firstname character varying
                (255),
    gender character varying
                (255),
    isdeleted boolean,
    joineddate timestamp without time zone,
    lastname character varying
                (255),
    password character varying
                (255),
    username character varying
                (255),
    locationid bigint,
    roleid bigint NOT NULL
);


                --
                -- TOC entry 4136 (class 2606 OID 25712)
                -- Name: assets assets_pkey; Type: CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.assets
                ADD CONSTRAINT assets_pkey PRIMARY KEY
                (assetcode);


                --
                -- TOC entry 4142 (class 2606 OID 25720)
                -- Name: assignments assignments_pkey; Type: CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.assignments
                ADD CONSTRAINT assignments_pkey PRIMARY KEY
                (assignmentid);


                --
                -- TOC entry 4144 (class 2606 OID 25728)
                -- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.categories
                ADD CONSTRAINT categories_pkey PRIMARY KEY
                (categorycode);


                --
                -- TOC entry 4149 (class 2606 OID 25733)
                -- Name: locations locations_pkey; Type: CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.locations
                ADD CONSTRAINT locations_pkey PRIMARY KEY
                (locationid);


                --
                -- TOC entry 4157 (class 2606 OID 25741)
                -- Name: requests requests_pkey; Type: CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.requests
                ADD CONSTRAINT requests_pkey PRIMARY KEY
                (requestid);


                --
                -- TOC entry 4160 (class 2606 OID 25746)
                -- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.roles
                ADD CONSTRAINT roles_pkey PRIMARY KEY
                (roleid);


                --
                -- TOC entry 4151 (class 2606 OID 25766)
                -- Name: locations uk6ythtk02shyto7rh04jhxi4h9; Type: CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.locations
                ADD CONSTRAINT uk6ythtk02shyto7rh04jhxi4h9 UNIQUE
                (address);


                --
                -- TOC entry 4147 (class 2606 OID 25764)
                -- Name: categories ukpr2ms98ayaf1r17k7yyr4l3o9; Type: CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.categories
                ADD CONSTRAINT ukpr2ms98ayaf1r17k7yyr4l3o9 UNIQUE
                (categoryname);


                --
                -- TOC entry 4162 (class 2606 OID 25776)
                -- Name: users ukr43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.users
                ADD CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE
                (username);


                --
                -- TOC entry 4167 (class 2606 OID 25754)
                -- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.users
                ADD CONSTRAINT users_pkey PRIMARY KEY
                (staffcode);


                --
                -- TOC entry 4132 (class 1259 OID 25756)
                -- Name: asset_category_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX asset_category_idx ON public.assets USING btree
                (categorycode);


                --
                -- TOC entry 4133 (class 1259 OID 25755)
                -- Name: asset_name_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX asset_name_idx ON public.assets USING btree
                (assetname);


                --
                -- TOC entry 4134 (class 1259 OID 25757)
                -- Name: asset_state_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX asset_state_idx ON public.assets USING btree
                (state);


                --
                -- TOC entry 4137 (class 1259 OID 25759)
                -- Name: assignment_assetcode_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX assignment_assetcode_idx ON public.assignments USING btree
                (assetcode);


                --
                -- TOC entry 4138 (class 1259 OID 25760)
                -- Name: assignment_assigneddate_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX assignment_assigneddate_idx ON public.assignments USING btree
                (assigneddate);


                --
                -- TOC entry 4139 (class 1259 OID 25758)
                -- Name: assignment_assignedto_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX assignment_assignedto_idx ON public.assignments USING btree
                (assignedto);


                --
                -- TOC entry 4140 (class 1259 OID 25761)
                -- Name: assignment_state_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX assignment_state_idx ON public.assignments USING btree
                (state);


                --
                -- TOC entry 4145 (class 1259 OID 25762)
                -- Name: category_name_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX category_name_idx ON public.categories USING btree
                (categoryname);


                --
                -- TOC entry 4152 (class 1259 OID 25770)
                -- Name: request_assignmentid_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX request_assignmentid_idx ON public.requests USING btree
                (assignmentid);


                --
                -- TOC entry 4153 (class 1259 OID 25767)
                -- Name: request_requestedby_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX request_requestedby_idx ON public.requests USING btree
                (requestedby);


                --
                -- TOC entry 4154 (class 1259 OID 25768)
                -- Name: request_returneddate_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX request_returneddate_idx ON public.requests USING btree
                (returneddate);


                --
                -- TOC entry 4155 (class 1259 OID 25769)
                -- Name: request_state_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX request_state_idx ON public.requests USING btree
                (state);


                --
                -- TOC entry 4158 (class 1259 OID 25771)
                -- Name: role_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX role_idx ON public.roles USING btree
                (rolename);


                --
                -- TOC entry 4163 (class 1259 OID 25774)
                -- Name: user_location_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX user_location_idx ON public.users USING btree
                (locationid);


                --
                -- TOC entry 4164 (class 1259 OID 25772)
                -- Name: user_role_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX user_role_idx ON public.users USING btree
                (roleid);


                --
                -- TOC entry 4165 (class 1259 OID 25773)
                -- Name: user_username_idx; Type: INDEX; Schema: public; Owner: -
                --

                CREATE INDEX user_username_idx ON public.users USING btree
                (username);


                --
                -- TOC entry 4175 (class 2606 OID 25818)
                -- Name: requests fk1ha6o2qgpjwvu7xftxcfdw0eq; Type: FK CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.requests
                ADD CONSTRAINT fk1ha6o2qgpjwvu7xftxcfdw0eq FOREIGN KEY
                (requestedby) REFERENCES public.users
                (staffcode);


                --
                -- TOC entry 4170 (class 2606 OID 25793)
                -- Name: assignments fk4d7j8tc7abcyqcnuswbv3ammf; Type: FK CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.assignments
                ADD CONSTRAINT fk4d7j8tc7abcyqcnuswbv3ammf FOREIGN KEY
                (assetcode) REFERENCES public.assets
                (assetcode);


                --
                -- TOC entry 4168 (class 2606 OID 25783)
                -- Name: assets fkbcv8pcymochk6ntks4kljh377; Type: FK CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.assets
                ADD CONSTRAINT fkbcv8pcymochk6ntks4kljh377 FOREIGN KEY
                (categorycode) REFERENCES public.categories
                (categorycode);


                --
                -- TOC entry 4173 (class 2606 OID 25808)
                -- Name: requests fke7a8ien8p1ywu34sk14kuyhp2; Type: FK CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.requests
                ADD CONSTRAINT fke7a8ien8p1ywu34sk14kuyhp2 FOREIGN KEY
                (acceptedby) REFERENCES public.users
                (staffcode);


                --
                -- TOC entry 4177 (class 2606 OID 25828)
                -- Name: users fkgrhs0suhl8cbodxn47xadxp94; Type: FK CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.users
                ADD CONSTRAINT fkgrhs0suhl8cbodxn47xadxp94 FOREIGN KEY
                (roleid) REFERENCES public.roles
                (roleid);


                --
                -- TOC entry 4169 (class 2606 OID 25788)
                -- Name: assets fkgrljitd2jsng6o7tjkgup5x33; Type: FK CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.assets
                ADD CONSTRAINT fkgrljitd2jsng6o7tjkgup5x33 FOREIGN KEY
                (locationid) REFERENCES public.locations
                (locationid);


                --
                -- TOC entry 4172 (class 2606 OID 25803)
                -- Name: assignments fkl6l8b6askq6oqrv4ray4gwbxc; Type: FK CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.assignments
                ADD CONSTRAINT fkl6l8b6askq6oqrv4ray4gwbxc FOREIGN KEY
                (assignedto) REFERENCES public.users
                (staffcode);


                --
                -- TOC entry 4171 (class 2606 OID 25798)
                -- Name: assignments fkmcdcffgmca59o2hi9fcskwcks; Type: FK CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.assignments
                ADD CONSTRAINT fkmcdcffgmca59o2hi9fcskwcks FOREIGN KEY
                (assignedby) REFERENCES public.users
                (staffcode);


                --
                -- TOC entry 4176 (class 2606 OID 25823)
                -- Name: users fknbgih6spq8ryw49q0yyyfvee7; Type: FK CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.users
                ADD CONSTRAINT fknbgih6spq8ryw49q0yyyfvee7 FOREIGN KEY
                (locationid) REFERENCES public.locations
                (locationid);


                --
                -- TOC entry 4174 (class 2606 OID 25813)
                -- Name: requests fkqpyo1upnqnraqpush9k502sph; Type: FK CONSTRAINT; Schema: public; Owner: -
                --

                ALTER TABLE ONLY public.requests
                ADD CONSTRAINT fkqpyo1upnqnraqpush9k502sph FOREIGN KEY
                (assignmentid) REFERENCES public.assignments
                (assignmentid);


-- Completed on 2021-08-12 10:48:12

--
-- PostgreSQL database dump complete
--
