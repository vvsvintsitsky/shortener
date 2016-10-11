CREATE TABLE "tag" (
	"id" serial NOT NULL,
	"description" character varying(255) NOT NULL UNIQUE,
	CONSTRAINT tag_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);




