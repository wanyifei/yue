<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* @author Chen Luo
**/

class Authmodel extends CI_Model
{
	function __construct()
	{
		$this -> load -> database();
	}

	public function sign_up($data)
	{
		$this -> db -> insert('user', $data);
		$this -> db -> select();
		$this -> db -> from('user');
		$this -> db -> where($data);
		$result = $this -> db -> get() -> result_array();
		return $result[0]['id'];
	}

	public function get_user_info($activity_id)
	{
		$this -> db -> select();
		$this -> db -> from('post_many');
		$this -> db -> where(array(
			'activity_id' => $activity_id,
			)
		);
		$result = $this -> db -> get() -> result_array();
		$user_id = $result[0]['user_id'];
		$this -> db -> select();
		$this -> db -> from('user');
		$this -> db -> where(array(
			'id' => $user_id,
			)
		);
		$user_info = $this -> db -> get() -> result_array();
		return $user_info;
	}

	public function get_picture_url($user_id)
	{
		$this -> db -> select('picture_url');
		$this -> db -> from('user');
		$this -> db -> where(array(
			'id' => $user_id,
			)
		);
		$result = $this -> db -> get() -> result_array();
		return $result[0]['picture_url'];
	}

	public function username_no_duplication($data)
	{
		$this -> db -> select();
		$this -> db -> from('user');
		$this -> db -> where(array(
			'username' => $data['username'],
			)
		);
		$result = $this -> db -> get() -> result_array();
		if(count($result) == 0)
			return 1;
		else
			return 0;
	}

	public function username_match($data)
	// -1 means not register, 0 means password is not right, 1 means succesfully.
	{
		$this -> db -> select('password');
		$this -> db -> from('user');
		$this -> db -> where(array(
			'username' => $data['username'],
			)
		);
		$result = $this -> db -> get() ->  result_array();

		if(!isset($result[0]))
			return -1;
		else if(password_verify($data['password'], $result[0]['password']))
			return 1;
		else
			return 0;
	}

	public function get_user_id($data)
	{
		$this -> db -> select();
		$this -> db -> from('user');
		$this -> db -> where(array(
			'username' => $data['username'],
			)
		);
		$result = $this -> db -> get() -> result_array();
		return $result[0]['id'];
	}
}