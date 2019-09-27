package top.givemefive.password;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;

public class MainActivity extends Activity
{
    TextView tv_single_pw=null;
    EditText ed_single_pw=null;
    TextView tv_multi_pw=null;
    EditText ed_multi_pw=null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_single_pw=findViewById(R.id.tx_showSinglePW);
        ed_single_pw=findViewById(R.id.ed_singlePassWord);
        Button btn_single=findViewById(R.id.btn_single);
        btn_single.setOnClickListener(new ShowListener());
        tv_multi_pw=findViewById(R.id.tx_showmultiPW);
        ed_multi_pw=findViewById(R.id.ed_multiPassWord);
        Button btn_multi=findViewById(R.id.btn_multi);
        btn_multi.setOnClickListener(new ShowListenerMulti());
    }
    private class ShowListener implements OnClickListener
    {

        @Override
        public void onClick(View p1)
        {
// TODO: Implement this method
            String str=ed_single_pw.getText().toString();
            char[] arr=str.toCharArray();
//String str_=String.valueOf(arr);//把字符数组转化成字符串
            int[] temp=new int[26];
            for(int i=0;i<arr.length;++i)
            {
                if(arr[i]<=97&&arr[i]>=65)
                {
                    temp[i]=arr[i]-65;
                    //tv_single_pw.setText(str_);
                }
                if(arr[i]<=122&&arr[i]>=97)
                {
                    temp[i]=arr[i]-97;
                }
            }
            int[] temp_=new int[64];
            char[] temp_ch=new char[64];
            for(int j=0;j<arr.length;++j)//加密过程
            {
                temp_[j]=(temp[j]+3)%26;//把加密后的ascii码存起来
                temp_ch[j]=(char)(temp_[j]+97);
            }
            String strTmp=String.valueOf(temp_ch);
            tv_single_pw.setText(strTmp);
        }
    }
    private class ShowListenerMulti implements OnClickListener
    {

        @Override
        public void onClick(View p1)
        {
// TODO: Implement this method
            char[] multi_arr=new char[25];//5*5矩阵
            int log=97;
            for(int i=0;i<25;++i)
            {
                if(log==106)//字母i=j
                    log=107;
                multi_arr[i]=(char)log;//初始化数组为26个字母
                ++log;
            }
//密钥fivestar
            char[] passwd={'f','i','v','e','s','t','a','r'};
            for(int i=0;i<24;)
            {
                for(int k=0;k<8;++k)
                {
                    if(multi_arr[i]==passwd[k])
                    {
                        for(int n=i;n<24;++n)
                        {
                            multi_arr[n]=multi_arr[n+1];//往前移动8个，剔除重复字母
                        }
                    }
                }
                ++i;
                for(int k=0;k<8;++k)
                {
                    if(multi_arr[i-1]==passwd[k])//整个代码最自豪的地方了
                        --i;
                }
            }
            for(int i=0;i<8;++i)
            {
                for(int j=23;j>=i;--j)
                {
                    multi_arr[j+1]=multi_arr[j];//往后移动8个，前面8个空位放密钥
                }
            }
            int log_pass_times=0;
            for(int i=0;i<25;++i)
            {
                if(log_pass_times==8)
                    break;
                multi_arr[i]= passwd[log_pass_times];//把8个密钥放到前面
                ++log_pass_times;
            }
//==========到这里一维构造矩阵成功==========//
            String str_mul_tmp=ed_multi_pw.getText().toString();
            char[] ch_mul=str_mul_tmp.toCharArray();
//==========单词开始分组===============//
            boolean bk=false;
            if((ch_mul.length%2)!=0)//奇数个字母
            {
//奇数则在最后插入字母y
                String str="加密奇数个字母的单词尚未解决！";
                tv_multi_pw.setText(str);
                bk=true;
            }
            if(bk==false){
                char[][] multi_arr_last=new char[6][6];
                int arr_x=0,arr_y=0;
                for(int i=0;i<multi_arr.length;++i,++arr_y)
                {
                    if(i==5||i==10||i==15||i==20||i==25)//该换行了
                    {
                        arr_y=0;
                        ++arr_x;
                    }
                    multi_arr_last[arr_x][arr_y]=multi_arr[i];
                }
                char[] arr_out=new char[64];//密文输出数组
                int one_x=0,one_y=0,two_x=0,two_y=0;
                for(int k=0;k<ch_mul.length;k+=2)//把EditText的字符串依次和矩阵比较
                {
                    for(int i=0;i<5;++i)
                    {
                        for(int j=0;j<5;++j)
                        {
                            if(ch_mul[k]==multi_arr_last[i][j])//记录下相等的数组下标
                            {
                                one_x=i;
                                one_y=j;
                            }
                        }
                    }
                    for(int i=0;i<5;++i)
                    {
                        for(int j=0;j<5;++j)
                        {
                            if(ch_mul[k+1]==multi_arr_last[i][j])//记录下相等的数组下标
                            {
                                two_x=i;
                                two_y=j;
                            }
                        }
                    }
                    if(one_x==two_x&&one_y!=two_y)//同一行
                    {
                        int tmp_one_y=one_y+1;
                        int tmp_two_y=two_y+1;
                        if(one_y==4)//第一列是右边最后一列的右边
                            tmp_one_y=0;
                        if(two_y==4)
                            tmp_two_y=0;
                        arr_out[k]=multi_arr_last[one_x][tmp_one_y];
                        arr_out[k+1]=multi_arr_last[two_x][tmp_two_y];
                    }
                    if(one_y==two_y&&one_x!=two_x)//同一列
                    {
                        int tmp_one_x=one_x+1;
                        int tmp_two_x=two_x+1;
                        if(one_x==4)//第一列是右边最后一列的右边
                            tmp_one_x=0;
                        if(two_x==4)
                            tmp_two_x=0;
                        arr_out[k]=multi_arr_last[tmp_one_x][one_y];
                        arr_out[k+1]=multi_arr_last[tmp_two_x][two_y];
                    }
//对角
                    if(one_x!=two_x&&one_y!=two_y)
                    {
//判断上下
                        if(one_x>two_x)//one在下
                        {
//判断左右====好像左右都一样
                            arr_out[k]=multi_arr_last[two_x][one_y];
                            arr_out[k+1]=multi_arr_last[one_x][two_y];
                        }
                        else//one在上
                        {
                            arr_out[k]=multi_arr_last[one_x][two_y];
                            arr_out[k+1]=multi_arr_last[two_x][one_y];
                        }
                    }
                    if(one_x==two_x&&one_y==two_y)//判断字母是否重复
                    {
//如果重复，在中间插入x
//String str="第偶数个字母和它下一个字母重复问题还没解决！";
                        arr_out[k]='-';
                        arr_out[k+1]='-';
                    }
                }
                String str_mul=String.valueOf(arr_out);
                tv_multi_pw.setText(str_mul);
            }
        }//bk
    }
}
