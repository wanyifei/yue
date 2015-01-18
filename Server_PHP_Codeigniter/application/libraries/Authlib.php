<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');
/**
* @author  Lim, Byunghoon <seian.hoon@gmail.com>
* 
*/
class Authlib
{
	function __construct()
	{
		$this -> ci =& get_instance();
		$this -> ci -> load -> library('session');
		$this -> ci -> load -> database();
	}

	function log_in($data)
	{
		$data['user_id'] = $data['id'];
		$this -> ci -> db -> insert('current_user', $data);
	}

	function get_user_id()
	{
		$this -> ci -> db -> select();
		$this -> ci -> db -> from('current _user');
		$user = $this -> ci -> db -> get() -> result_array();
		return $user[0]['user_id'];
	}

	function log_out()
	{
		$this -> ci -> db -> sess_destroy();
	}

	function is_log_in()
	{
		return $this -> ci -> session -> userdata('is_login');
	}

}