<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* @author Chen Luo
**/

class Activitymodel extends CI_Model
{
	function __construct()
	{
		$this -> load -> database();
	}


	public function get_receiver_id($activity_id)
	{
		$this -> db -> select('user_id');
		$this -> db -> from('post_many');
		$this -> db -> where(array(
			'activity_id' => $activity_id,
			)
		);

		$result = $this -> db -> get() -> result_array();
		return $result[0]['user_id'];
	}


	public function agree_join($data)
	{
		$this -> db -> where(array(
			'sender_id' => $data['sender_id'],
			'receiver_id' => $data['receiver_id'],
			'activity_id' => $data['activity_id'],
			)
		);
		$this -> db -> update('notification', array(
			'status' => 1,
			)
		);
		$this -> db -> insert('join_many', array(
			'activity_id' => $data['activity_id'],
			'user_id' => $data['sender_id'],
			)
		);
	}

	public function decline_join($data)
	{
		$this -> db -> where(array(
			'sender_id' => $data['sender_id'],
			'receiver_id' => $data['receiver_id'],
			'activity_id' => $data['activity_id'],
			)
		);
		$this -> db -> update('notification', array(
			'status' => 2,
			)
		);
	}

	public function insert_notification($data)
	{
		$this -> db -> insert('notification', $data);
	}

	public function get_activity_title($activity_id)
	{

		$this -> db -> select('title');
		$this -> db -> from('activity');
		$this -> db -> where(array(
			'id' => $activity_id,
			)
		);

		$result = $this -> db -> get() -> result_array();
		return $result[0]['title'];
	}

	public function get_activities($data)
	{
		$this -> db -> select();
		$this -> db -> from('activity');
		$this -> db -> where(array(
			'category' => $data['category'],
			)
		);
		$result = $this -> db -> get() -> result_array();
		return $result;
	}

	public function post_activity($data, $current_user_id)
	{
		$this -> db -> insert('activity', $data);
		$this -> db -> select();
		$this -> db -> from('activity');
		$this -> db -> where($data);
		$activity = $this -> db -> get() -> result_array();
		$this -> db -> insert('post_many', array(
			'user_id' => $current_user_id,
			'activity_id' => $activity[0]['id'],
			)
		);
	}

	public function get_post_activities($data)
	{
		$this -> db -> select('activity_id');
		$this -> db -> from('post_many');
		$this -> db -> where(array(
			'user_id' => $data['user_id'],
			)
		);
		$activity_id_list = $this -> db -> get() -> result_array();
		$result = array();
		foreach($activity_id_list as $activity_id)
		{
			$this -> db -> select();
			$this -> db -> from('activity');
			$this -> db -> where(array(
				'id' => $activity_id['activity_id'],
				)
			);
			$activity = $this -> db -> get() -> result_array();
			array_push($result, $activity[0]);
		}
		return $result;
	}

	public function get_join_activities($data)
	{
		$this -> db -> select('activity_id');
		$this -> db -> from('join_many');
		$this -> db -> where(array(
			'user_id' => $data['user_id'],
			)
		);
		$activity_id_list = $this -> db -> get() -> result_array();
		$result = array();
		foreach($activity_id_list as $activity_id)
		{
			$this -> db -> select();
			$this -> db -> from('activity');
			$this -> db -> where(array(
				'id' => $activity_id['activity_id'],
				)
			);
			$activity = $this -> db -> get() -> result_array();
			array_push($result, $activity[0]);
		}
		return $result;
	}

	public function get_notifications($data)
	{
		$this -> db -> select();
		$this -> db -> from('notification');
		$this -> db -> where(array(
			'receiver_id' => $data['current_user_id'],
			'status' => 0,
			)
		);
		$this -> db -> join('user', 'user.id = sender_id');
		$result = $this -> db -> get() -> result_array();
		return $result;
	}

}