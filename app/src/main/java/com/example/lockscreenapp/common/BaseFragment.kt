package com.example.lockscreenapp.common

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.example.lockscreenapp.ultis.findNavControllerSafely

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    private var nameScreen = ""
    private var fragmentStarted = false
    private var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameScreen = getNameScreen()
        setupData()
        setupUI()
        setupListener()
    }

    protected abstract val layoutId: Int
    protected lateinit var binding: T
    private var dialog: Dialog? = null
    private val pendingOperations: ArrayList<Runnable> = ArrayList()

    override fun onResume() {
        super.onResume()
        fragmentStarted = true
        for (pendingOperation in pendingOperations) {
            pendingOperation.run()
        }
        pendingOperations.clear()
    }

    protected open fun setupUI() {}
    protected open fun setupData() {}
    protected open fun setupListener() {}
    fun requireContext(action: (nonNullContext: Context) -> Unit) {
        context?.let(action)
    }

    fun addOperation(op: Runnable) {
        if (!fragmentStarted) {
            pendingOperations.add(op)
        } else {
            op.run()
        }
    }

    fun toast(message: String?, isLong: Boolean? = false) {
        context?.let {
            toast?.cancel()
            toast = Toast.makeText(
                it,
                message,
                if (isLong == false) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
            )
            toast?.show()
        }
    }

    fun toastDebug(message: String, isLong: Boolean? = false) {
        context?.let {
            toast?.cancel()
            toast = Toast.makeText(
                it,
                message,
                if (isLong == false) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
            )
            toast?.show()
        }
    }

    private fun getNameScreen(): String {
        val input = this.javaClass.simpleName.replace("Fragment", "Screen", ignoreCase = true)
        val result = StringBuilder()
        for (char in input) {
            if (char.isUpperCase()) {
                result.append('_').append(char.lowercaseChar())
            } else {
                result.append(char)
            }
        }
        return if (result.isNotEmpty() && result[0] == '_') result.substring(1) else result.toString()
    }

    fun NavController.safeNavigateWithArgs(direction: NavDirections, bundle: Bundle? = Bundle()) {
        currentDestination?.getAction(direction.actionId)?.run {
            navigate(direction.actionId, bundle)
        }
    }

    fun navigateToDestination(destination: Int) {
        try {
            findNavControllerSafely()?.navigate(destination)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun navigateToDestination(destination: Int, bundle: Bundle) {
        try {
            findNavControllerSafely()?.navigate(destination, bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun navigateToDestination(destination: NavDirections) {
        try {
            findNavControllerSafely()?.navigate(destination)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
