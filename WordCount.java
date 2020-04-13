import java.io.IOException;
import java.util.*;
import java.util.regex.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;


public class WordCount {

	public static void main(String[] args)
		throws IOException, ClassNotFoundException, InterruptedException {
			if(args.length != 2) {
				System.err.println("Usage: Word Count <input path> <output path>);");
				System.exit(-1);
			}
		Job job = new Job();
		job.setJarByClass(WordCount.class);
		job.setJobName("WordCount");
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(PleaseWorkMapper.class);
		job.setReducerClass(PleaseWorkReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.waitForCompletion(true);
	}
}

class PleaseWorkMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	private Text word = new Text();

    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException
    {
        String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();
        String line = value.toString();
        StringTokenizer tokenizer = new StringTokenizer(line);

        while (tokenizer.hasMoreTokens())
        {
            word.set(tokenizer.nextToken());
            context.write(word, new Text (fileName));
        }
    }
}

class PleaseWorkReducer extends Reducer<Text, Text, Text, Text>
{
	public void reduce(Text key, Iterable<Text> values, Context context)
		throws IOException, InterruptedException
	{
		HashMap<String, Integer> output = new HashMap<>();

        for (Text text: values) {
            String word = text.toString();
            if (output == null || output.get(word) == null) {
                output.put(word, 1);
            } else {
                output.put(word, ((int) output.get(word)) + 1);
            }
        }

        context.write(key, new Text(output.toString()));
    }
}

