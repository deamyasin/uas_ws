<?php class Coupons_model extends CI_Model{
    protected $table_name;
    protected $primary_key;
    public function __construct()
    {
        parent::__construct();
        $this->table_name = 'coupons';
        $this->primary_key= 'coupon_id';
    }
	
    function get($filter = array(),$search = "",$offcet="",$limit=""){
        if(!empty($filter))
        {
            if(isset($filter["user_id"])){
                $this->db->where_in("users",array("0","",$filter["user_id"]));
                unset($filter["user_id"]);
            }
            if(isset($filter["validity"])){
                $this->db->where("validity_start <=",date(MYSQL_DATE_FORMATE,strtotime($filter["validity"])));
                $this->db->where("validity_end >=",date(MYSQL_DATE_FORMATE,strtotime($filter["validity"])));
                
                unset($filter["validity"]);
            }
            $this->db->where($filter);
        }
        if($search != ""){
            $this->db->or_like(array($this->table_name.'.coupon_code'=>$search,
                                    $this->table_name.'.description_en'=>$search,
                                    $this->table_name.'.description_ar'=>$search));
        }
		$this->db->select("{$this->table_name}.*");
        $this->db->where($this->table_name.".draft","0");
		$this->db->order_by($this->table_name.".".$this->primary_key." desc");
        if($offcet !="" && $limit != ""){
            $this->db->limit($limit,$offcet);
        }
        $q = $this->db->get($this->table_name);
        return $q->result();

    }

    function get_by_id($id){
        $this->db->where($this->table_name.".".$this->primary_key,$id);
        $q = $this->db->get($this->table_name);
        return $q->row();
    }

    function validate($user_id,$coupon_code){
        $date = date(MYSQL_DATE_FORMATE);
        $coupons = $this->coupons_model->get(array("user_id"=>$user_id,"coupon_code"=>$coupon_code,"validity"=>$date));
        if(empty($coupons)){
            return array(
                RESPONCE => false,
                MESSAGE => _l("Opps! Sorry Coupon code not valid"),
                DATA => _l("Opps! Sorry Coupon code not valid"),
                CODE => 101
            );
        }else{
            $coupon = $coupons[0];
            if($coupon->multi_usage != 1){
                $this->load->model("orders_model");
                $order = $this->orders_model->get(array("orders.user_id"=>$user_id,"orders.coupon_code"=>$coupon_code));
                return array(
                    RESPONCE => false,
                    MESSAGE => _l("Sorry you already applied this code"),
                    DATA => _l("Sorry you already applied this code"),
                    CODE => 101
                );
            }

            return array(
                RESPONCE => true,
                MESSAGE => _l("Coupon vlaidated"),
                DATA => $coupon,
                CODE => CODE_SUCCESS
            );
        }
    }
}
