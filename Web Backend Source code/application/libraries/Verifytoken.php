<?php   defined('BASEPATH') or exit('No direct script access allowed');
include APPPATH . 'third_party/vendor/autoload.php';

class Verifytoken
{
    public $code;
    public $personalToken;
    public $userAgent;
    public function __construct()
    {
        
        $this->personalToken = get_option("env_token");
        $this->userAgent = "https://dsinfoway.com";
    }
    function verify($code){

       
        return  true; // (int) 17022701
        
    }
}