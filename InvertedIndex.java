import java.io.IOException;
import java.util.*;
import java.util.regex.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;


public class InvertedIndex {

	public static void main(String[] args)
		throws IOException, ClassNotFoundException, InterruptedException {
			if(args.length != 2) {
				System.err.println("Usage: Word Count <input path> <output path>);");
				System.exit(-1);
			}
		Job job = new Job();
		job.setJarByClass(InvertedIndex.class);
		job.setJobName("InvertedIndex");
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(InvertedIndexMapper.class);
		job.setReducerClass(InvertedIndexReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.waitForCompletion(true);
	}
}

class InvertedIndexMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	private Text word = new Text();

    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException
    {
        String line = value.toString();
		StringTokenizer tokenizer = new StringTokenizer(line);
		Text docID = new Text(((FileSplit) context.getInputSplit()).getPath().toString().replace("gs://dataproc-staging-us-west1-56546323985-2mifuxiv/data/", ""));  
		Pattern pattern = Pattern.compile("[\\w].*[\\w]");
		Matcher matcher;
		
		while (tokenizer.hasMoreTokens()) {
			matcher = pattern.matcher(tokenizer.nextToken());
			if (matcher.find()) {	
				word.set(matcher.group(0).toLowerCase().trim());
				context.write(word, docID);
			}
}		}
}

class InvertedIndexReducer extends Reducer<Text, Text, Text, Text>
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

