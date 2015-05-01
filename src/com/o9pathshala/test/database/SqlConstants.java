package com.o9pathshala.test.database;

public interface SqlConstants {
	String TEST_LIST = "select test_name,test_id from o9_INSTITUTE_ID_test";
	String COUNT_RESULTS = "select count(*) as count from o9_INSTITUTE_ID_results_view where test_id=? and batch_id=? and student_name";
	String GET_RESULTS = "select student_id,attempt,score,attempt_date,test_name,student_name,batch_name from o9_INSTITUTE_ID_results_view where test_id=? and batch_id=? and student_name";
	String GET_MY_RESULT = "select test_name,score from o9_INSTITUTE_ID_results_view where student_id=? order by attempt_date asc";
	String GET_FACULTY_TESTS = "select * from o9_INSTITUTE_ID_test where test_created_by_id=? and test_name";
	String COUNT_FACULTY_TEST = "select count(*) as count from o9_INSTITUTE_ID_test where test_created_by_id=? and test_name";
	String COUNT_STUDENT_TEST = "select count(*) as count from o9_INSTITUTE_ID_test_batch_mapping a left join o9_INSTITUTE_ID_test b on a.test_id=b.test_id where  a.batch_id='BATCH_ID' and b.test_name";
	String GET_ATTEMPTED_TEST = "select test_id,test_name from o9_INSTITUTE_ID_results_view where student_id='STUDENT_ID'";
	String GET_STUDENT_TEST = "select b.test_id,b.test_name,b.test_duration,b.test_start_date,b.test_end_date,b.test_positive_mark,b.test_negative_mark,test_created_by_name from `o9_INSTITUTE_ID_test_batch_mapping` a left join `o9_INSTITUTE_ID_test` b on a.test_id=b.test_id where  a.batch_id='BATCH_ID' and b.test_activation='ACTIVATION' order by b.test_upload_time desc";
	String START_TEST = "select * from (select * from o9_INSTITUTE_ID_my_test_view  union select * from o9_INSTITUTE_ID_purchased_test_view ) t where test_id='TEST_ID' order by section_name";
	String GET_TEST = "select * from o9_INSTITUTE_ID_test where test_id='TEST_ID' and test_activation='1'";
	String SET_RESULT = "insert into o9_INSTITUTE_ID_results (test_id, test_name, score, batch_id, student_id, attempt) values ('TEST_ID', 'TEST_NAME', 'SCORE', 'BATCH_ID', 'STUDENT_ID', (select COALESCE(MAX(attempt), 0) + 1 from o9_INSTITUTE_ID_results_view where test_id = 'TEST_ID' and student_id = 'STUDENT_ID'))";
	String GET_PROFILE_PIC = "IP/o9pathshala/users/INSTITUTE_ID/images/savedfiles/USER_ID.jpg";
	String MY_RANK = "select student_id,student_name,score from o9_INSTITUTE_ID_results_view where test_id='TEST_ID' order by attempt asc, score desc,attempt_date asc" ;
}
