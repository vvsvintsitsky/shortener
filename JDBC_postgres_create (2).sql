CREATE TABLE "account" (
	"id" serial NOT NULL,
	"name" character varying(50) NOT NULL,
	"password" character varying(50) NOT NULL,
	CONSTRAINT account_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);




