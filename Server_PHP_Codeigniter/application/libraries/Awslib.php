<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

require 'aws-sdk/aws-autoloader.php';
use Aws\Common\Aws;
use Aws\S3\S3Client;

/**
* @author  Chen Luo
*/

class Awslib
{
	function __construct($service)
    {
        $this -> ci =& get_instance();
        $this -> ci -> load -> library('authlib');
        $this -> ci -> load -> database();

        $config = array();
        $config['key'] = 'AKIAJDJV24VZHDNCC42A';
        $config['secret'] = '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';
            
        if($service['name'] == 's3') {
            $this -> s3 = S3Client::factory($config);
            //$this -> s3 -> disable_ssl_verification();  
        }
    }

    public function s3_upload_file($data)
    {
    	$suffix = strrchr($data['file_name'], '.');
    	$title = md5($data['user_id'].time().rand(1, getrandmax())).$suffix;

    	$picture = array();
    	$picture['Bucket'] = 'hangoutbucket';
    	$picture['Key'] = 'UserPicture/'.$title;
    	$picture['SourceFile'] = $data['file_content'];
    	$object = $this -> s3 -> putObject($picture);

    	$result = array();
    	$result['picture_url'] = $object['ObjectURL'];
    	return $result;
    }
}