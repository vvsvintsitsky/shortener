CREATE TABLE "url" (
	"id" serial NOT NULL,
	"short_url" character varying(10) NOT NULL UNIQUE,
	"long_url" character varying(255) NOT NULL,
	"description" character varying(255) NOT NULL,
	"visited" int NOT NULL,
	"account_id" int NOT NULL,
	CONSTRAINT url_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);




