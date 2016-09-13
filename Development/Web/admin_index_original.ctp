<div class="bc">
            <ul id="breadcrumbs" class="breadcrumbs">
                 <li>
                      <?php echo $this->Html->link('Orders',
				 						array('controller'=>'Orders',
											  'action'=>'index'),
										array('class'=>'add_item'));?>

                 </li>


                 <li class="current"><a href="#">Current page</a></li>
            </ul>
            <div class="clear"></div>
        </div>

  <!-- Title area -->
<div class="titleArea">
    <div class="wrapper">
        <div class="pageTitle">
            <h4> Manage Open Orders</h4>
        </div>
        <div class="middleNav">
            	<ul>
                 <li><?php echo $this->Html->link('View Closed Orders',
				 						array('controller'=>'Orders',
											  'action'=>'closed'),
										array('class'=>'add_item'));?></li>


                </ul>
                <div class="clear"></div>
            </div>
        <div class="clear"></div>
    </div>
</div>

<div class="wrapper">
<div class="widget">
     <div class="title"><?php  echo $this->Html->image('admin/icons/dark/timer.png', array('class' => 'titleIcon'));?><h6>Orders</h6></div>
<table cellpadding="0" cellspacing="0" width="100%" class="dTable display">
      <thead>
          <tr>
            <td>#</td>
            <td>Reference Number</td>
            <td>Customer</td>
            <td>Order subtotal</td>
            <td>Shop Branch</td>
            <td>Shop Brand</td>
            <td>Created</td>
            <td></td>
       </tr>
   </thead>
   <tbody>
    <?php
	$count = 1;
	//print_r($items);
	foreach($items as $item):

	?>
		<tr class="gradeX">
            <td width="20"><?php echo $count; ?></td>
            <td><?php echo $item['Order']['ref_number']; ?></td>
            <td><?php echo $item['Order']['first_name'].' '.$item['Order']['last_name']; ?> - <?php echo $item['Order']['phone']; ?></td>
            <td>
             <?php
			 $stack = array();

			$new_array = json_decode($item['Order']['user_order']);

			 foreach($new_array as $key=>$value){


						 $totalprice = $value->price * $value->qty;

						 array_push($stack, $totalprice);

			}




			 $money =array_sum($stack);

			echo 'Ksh '.number_format($money, 2, '.', '');
			 ?>
            </td>

       <td><?php echo $this->requestAction(array('controller'=>'App','action'=>'getItemTitle', 'ShopsBrand', $item['Order']['shop_brand_id'])); ?> </td>
             <td><?php echo $this->requestAction(array('controller'=>'App','action'=>'getItemTitle', 'ShopsBranch', $item['Order']['shop_branch_id'])); ?> </td>
            <td><?php $date = date_create($item['Order']['created']); echo date_format($date, 'D, jS M Y , g:ia');  ?></td>
            <td>

                   <?php echo $this->Html->link('More Details',
array('controller' => 'orders', 'action' => 'view', $item['Order']['id'])); ?>
                                      </td>
        </tr>
	<?php
	$count++;
    endforeach; ?>
 </tbody>
          </table>
</div></div>
