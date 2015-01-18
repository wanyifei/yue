<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* @author  Chen Luo
*/

class User extends CI_Controller
{
	function __construct()
	{
		parent::__construct();

		$this -> load -> model('authmodel');
		//$this -> load -> model('picturemodel');
		//$this -> load -> library('awslib', array('name' => 's3'));
	}


	public function sign_up()
	{
		$data['username'] = $this -> input -> post('username');
		if($this -> authmodel -> username_no_duplication($data))
		{
			$data['phone_number'] = $this -> input -> post('phone_number');
			$data['password'] = password_hash($this -> input -> post('password'), PASSWORD_DEFAULT);
			$data['email_address'] = $this -> input -> post('email');

			if($this -> input -> post('gender') == 'Male')
				$data['gender'] = 0;
			elseif($this -> input -> post('gender') == 'Female')
				$data['gender'] = 1;
			else
				$data['gender'] = 2;

			$data['picture_url'] = $this -> input -> post('picture_url');
			$current_user_id = $this -> authmodel -> sign_up($data);
			echo json_encode(array(array('is_successful' => 1, 'current_user_id' => $current_user_id)));
		}
		else
			echo json_encode(array(array('is_successful' => 0)));
	}

	public function get_user_info()
	{
		$result = $this -> authmodel -> get_user_info($this -> input -> post('activity_id'));
		echo json_encode($result);
	}


	public function log_in()
	{
		$data['username'] = $this -> input -> post('username');

		$data['password'] = $this -> input -> post('password');

		$login_result = $this -> authmodel -> username_match($data);
		if($login_result == 1)
		{
			$data['id'] = $this -> authmodel -> get_user_id($data);
			echo json_encode(array(array('is_successful' => 1, "current_user_id" => $data['id'])));
		}
		elseif($login_result == 0)
			echo json_encode(array(array('is_successful' => 0, 'fail_reason' => 'Incorrect password!')));
		else
			echo json_encode(array(array('is_successful' => 0, 'fail_reason' => 'Unrecognized username!')));
	}

/*	public function upload_picture()
	{
		$current_user_id = $this -> input -> post('current_user_id');
		$file_name = $_FILES[0]['name'];
		$file_content = $_FILES[0]['temp_name'];
		$res = $this -> awslib -> s3_upload($current_user_id, $file_name, $file_content);
		$res['current_user_id'] = $current_user_id;
		$this -> picturemodel -> attach_picture($res);
	}*/
}