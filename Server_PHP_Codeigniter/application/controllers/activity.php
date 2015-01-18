<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* @author  Chen Luo
*/

class Activity extends CI_Controller
{
	function __construct()
	{
		parent::__construct();
		$this -> load -> model('activitymodel');
		$this -> load -> model('authmodel');
	}

	public function get_activities()
	{
		$category = $this -> input -> post('category');
		switch($category)
		{
			case 'food':
				$data['category'] = 0;
				break;
			case 'sport':
				$data['category'] = 1;
				break;
			case 'study':
				$data['category'] = 2;
				break;
			case 'movie':
				$data['category'] = 3;
				break;
			case 'other':
				$data['category'] = 4;
				break;
		}
		$result = $this -> activitymodel -> get_activities($data);
		echo json_encode($result);
	}

	public function post_activity()
	{
		$current_user_id = $this -> input -> post('current_user_id');
		$data['title'] = $this -> input -> post('title');
		$data['time_month'] = $this -> input -> post('time_month');
		$data['time_day'] = $this -> input -> post('time_day');
		$data['time_hour'] = $this -> input -> post('time_hour');
		$data['time_minute'] = $this -> input -> post('time_minute');
		$data['dest_lat'] = $this -> input -> post('dest_lat');
		$data['dest_lgt'] = $this -> input -> post('dest_lgt');
		$data['dest_addr'] = $this -> input -> post('dest_addr');
		$data['depart_lat'] = $this -> input -> post('depart_lat');
		$data['depart_lgt'] = $this -> input -> post('depart_lgt');
		$data['depart_addr'] = $this -> input -> post('depart_addr');
		$data['description'] = $this -> input -> post('description');
		switch($this -> input -> post('category'))
		{
			case 'food':
				$data['category'] = 0;
				break;
			case 'sports':
				$data['category'] = 1;
				break;
			case 'study':
				$data['category'] = 2;
				break;
			case 'movie':
				$data['category'] = 3;
				break;
			case 'other':
				$data['category'] = 4;
				break;
		}
		$this -> activitymodel -> post_activity($data, $current_user_id);
		echo json_encode(array(array('is_successful' => 1)));
	}


	public function join_activity()
	{
		$data['sender_id'] = $this -> input -> post('current_user_id');
		$data['activity_id'] = $this -> input -> post('activity_id');
		$data['receiver_id'] = $this -> activitymodel -> get_receiver_id($data['activity_id']);
		$data['activity_title'] = $this -> activitymodel -> get_activity_title($data['activity_id']);
		$data['status'] = 0;
		$data['picture_url'] = $this -> authmodel -> get_picture_url($data['sender_id']);
		$this -> activitymodel -> insert_notification($data);
		echo json_encode(array(array('is_successful' => 1)));
	}

	public function agree_join()
	{
		$data['activity_id'] = $this -> input -> post('activity_id');
		$data['sender_id'] = $this -> input -> post('sender_id');
		$data['receiver_id'] = $this -> input -> post('current_user_id');
		$this -> activitymodel -> agree_join($data);
		echo json_encode(array(array('is_successful' => 1)));
	}


	public function decline_join()
	{
		$data['activity_id'] = $this -> input -> post('activity_id');
		$data['sender_id'] = $this -> input -> post('sender_id');
		$data['receiver_id'] = $this -> input -> post('current_user_id');
		$this -> activitymodel -> decline_join($data);
		echo json_encode(array(array('is_successful' => 1)));
	}

	public function get_post_activities()
	{
		$data['user_id'] = $this -> input -> post('current_user_id');
		$result = $this -> activitymodel -> get_post_activities($data);
		echo json_encode($result);
	}

	public function get_join_activities()
	{
		$data['user_id'] = $this -> input -> post('current_user_id');
		$result = $this -> activitymodel -> get_join_activities($data);
		echo json_encode($result);
	}

	public function get_notifications()
	{
		$data['current_user_id'] = $this -> input -> post('current_user_id');
		$result = $this -> activitymodel -> get_notifications($data);
		echo json_encode($result);
	}

}