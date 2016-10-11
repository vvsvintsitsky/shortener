CREATE TABLE "account" (
	"id" serial NOT NULL,
	"email" character varying(50) NOT NULL UNIQUE,
	"password" character varying(50) NOT NULL,
	"created" TIMESTAMP NOT NULL,
	"is_notified" BOOLEAN NOT NULL,
	"is_confirmed" BOOLEAN NOT NULL,
	CONSTRAINT account_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);




