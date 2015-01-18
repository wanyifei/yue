<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* @author Chen Luo
**/

class Picturemodel extends CI_Model
{
	function __construct()
	{
		parent::__construct();

		$this -> load -> database();
	}

	public function get_picture_url($user_id)
	{
		$this -> db -> select('picture_url');
		$this -> db -> from('user');
		$this -> db -> where(array(
			'user_id' => $user_id,
			)
		);
		$result = $this -> db -> get() -> result_array();
		return $result;
	}

	public function attach_picture($data)
	{
		$this -> db -> where(array('user_id' => $data['current_user_id']));
		$this -> db -> update('user', array('picture_url' => $data['picture_url']));
	}
}