CREATE TABLE "account" (
	"id" serial NOT NULL,
	"email" character varying(50) NOT NULL,
	"password" character varying(50) NOT NULL,
	CONSTRAINT account_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "url" (
	"id" serial NOT NULL,
	"short_url" character varying(10) NOT NULL,
	"long_url" character varying(255) NOT NULL,
	"description" character varying(255) NOT NULL,
	"visited" int NOT NULL,
	CONSTRAINT url_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "tag" (
	"id" serial NOT NULL,
	"description" character varying(255) NOT NULL,
	CONSTRAINT tag_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "url_2_tag" (
	"url_id" int NOT NULL,
	"tag_id" int NOT NULL
) WITH (
  OIDS=FALSE
);






ALTER TABLE "url_2_tag" ADD CONSTRAINT "url_2_tag_fk0" FOREIGN KEY ("url_id") REFERENCES "url"("id");
ALTER TABLE "url_2_tag" ADD CONSTRAINT "url_2_tag_fk1" FOREIGN KEY ("tag_id") REFERENCES "tag"("id");

